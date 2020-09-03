import pickle

from sklearn import preprocessing
from sklearn.discriminant_analysis import LinearDiscriminantAnalysis
from sklearn.model_selection import train_test_split

import download_dataset
import visualisation
import preprocess
import models.sgd
import models.svm
import models.vnn
import models.cnn

import pandas as pd



def main():
    # training labels
    train = pd.read_csv("submission/train_post_competition.csv")
    # sample submission
    sample_submission = pd.read_csv("submission/" + "test_post_competition_scoring_clips.csv")
    sample_submission = sample_submission[['fname']].copy()
    #
    # # download data, organise directory and install packages
    # download_dataset.exec()
    # # plot category vs. no. of samples
    # visualisation.categoryVsSample(train)
    #
    # # preprocess and compute mel specs and metrics
    # preprocess.preprocess(train, sample_submission)
    # preprocess.computeLogMel(train, sample_submission)
    # preprocess.computeMetrics(train, sample_submission)

    # get files from original
    file_to_tag = pd.Series(train['label'].values, index=train['fname']).to_dict()

    def getTag(x):
        return file_to_tag[x]

    # read in training features
    pickle_in = open("data/train_tab_feats.pkl", "rb")
    df_train = pickle.load(pickle_in)
    # read in testing features
    pickle_in = open("data/test_tab_feats.pkl", "rb")
    df_test = pickle.load(pickle_in)
    # combine
    total = pd.concat([df_train, df_test], ignore_index=True)

    # get usuable test file
    df_train['tag'] = df_train['fname'].apply(getTag)
    df_train_copy = df_train.drop(['fname', 'tag'], axis=1)

    # reduce dimensions and make train val sets
    LDA = LinearDiscriminantAnalysis()
    X = LDA.fit_transform(df_train_copy, df_train['tag'])

    x_train, x_val, y_train, y_val = train_test_split(X, df_train['tag'], shuffle=True, test_size=0.2, random_state=42)

    encoder = preprocessing.LabelEncoder()
    y_train = encoder.fit_transform(y_train)
    y_val = encoder.fit_transform(y_val)

    # stochastic gradient descent model
    sgd = models.sgd.fit(x_train, y_train, x_val, y_val, encoder)

    # support vector machine
    svm = models.svm.fit(x_train, y_train, x_val, y_val, encoder, None)

    # grid search on SVM
    best_parameters, accuracy, test_accuracy = models.svm.grid_search(x_train, y_train, x_val, y_val)
    best_svm = models.svm.fit(x_train, y_train, x_val, y_val, encoder, best_parameters)

    # vanilla neural network
    vnn = models.vnn.fit(x_train, y_train, x_val, y_val)

    # convolutional neural network
    cnn = models.cnn.fit(x_train, y_train, x_val, y_val)

    print("\n\n-------------------------------SGD-------------------------------\n", sgd)
    print("\n\n-------------------------------SVM-------------------------------\n", svm)
    print("\n\n-----------------------------Best SGD----------------------------\n", best_svm)
    print("\n\n----------------------------Vanilla NN---------------------------\n", vnn)
    print("\n\n-------------------------------CNN-------------------------------\n", cnn)





if __name__ == '__main__':
    main()