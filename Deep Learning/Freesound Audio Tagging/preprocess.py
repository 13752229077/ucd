import os

import librosa
import numpy as np
import pandas as pd

from joblib import Parallel, delayed
from summary_feats_funcs import all_feats
from tqdm import tqdm



def preprocess(train, sample_submission, sr=44100):
    """Preprocessing"""

    # Ignoring the empty wavs
    sample_submission['toremove'] = 0
    sample_submission.loc[sample_submission.fname.isin([
        '0b0427e2.wav', '6ea0099f.wav', 'b39975f5.wav'
    ]), 'toremove'] = 1

    print('Train...')
    os.makedirs('data/audio_train_trim/', exist_ok=True)
    for filename in tqdm(train.fname.values):
        x, sr = librosa.load('data/train/' + filename, sr)
        x = librosa.effects.trim(x)[0]
        np.save('data/audio_train_trim/' + filename + '.npy', x)

    print('Test...')
    os.makedirs('data/audio_test_trim/', exist_ok=True)
    for filename in tqdm(sample_submission.loc[lambda x: x.toremove == 0, :].fname.values):
        x, sr = librosa.load('data/test/' + filename, sr)
        x = librosa.effects.trim(x)[0]
        np.save('data/audio_test_trim/' + filename + '.npy', x)


def compute_melspec(filename, indir, outdir):
    wav = np.load(indir + filename + '.npy')
    wav = librosa.resample(wav, 44100, 22050)
    melspec = librosa.feature.melspectrogram(wav,
                                             sr=22050,
                                             n_fft=1764,
                                             hop_length=220,
                                             n_mels=64)
    logmel = librosa.core.power_to_db(melspec)
    np.save(outdir + filename + '.npy', logmel)


def computeLogMel(train, sample_submission):
    """Compute Log Mel-Spectrograms"""

    # calculate log mel: https://datascience.stackexchange.com/questions/27634/how-to-convert-a-mel-spectrogram-to-log-scaled-mel-spectrogram
    # Paper: https://arxiv.org/pdf/1608.04363.pdf



    print('Train...')
    os.makedirs('data/mel_spec_train', exist_ok=True)
    for x in tqdm(train.fname.values):
        compute_melspec(x, 'data/audio_train_trim/', 'data/mel_spec_train/')

    os.makedirs('data/mel_spec_test/', exist_ok=True)
    for x in tqdm(sample_submission.loc[lambda x: x.toremove == 0, :].fname.values):
        compute_melspec(x, 'data/audio_test_trim/', 'data/mel_spec_test/')


def computeMetrics(train, sample_submission):
    # number of cores to use (-1 uses all)
    num_cores = -1

    print('Train...')


    train_feats = Parallel(n_jobs=num_cores)(
        delayed(all_feats)('data/audio_train_trim/' + x + '.npy')
        for x in tqdm(train.fname.values))

    train_feats_df = pd.DataFrame(np.vstack(train_feats))
    train_feats_df['fname'] = pd.Series(train.fname.values, index=train_feats_df.index)
    train_feats_df.to_pickle('data/train_tab_feats.pkl')

    print('Test...')

    test_feats = Parallel(n_jobs=num_cores)(
        delayed(all_feats)('data/audio_test_trim/' + x + '.npy')
        for x in tqdm(sample_submission
                      .loc[lambda x: x.toremove == 0, :]
                      .fname.values))

    test_feats_df = pd.DataFrame(np.vstack(test_feats))
    test_feats_df['fname'] = pd.Series(sample_submission.loc[lambda x: x.toremove == 0, :].fname.values,
                                       index=test_feats_df.index)
    test_feats_df.to_pickle('data/test_tab_feats.pkl')