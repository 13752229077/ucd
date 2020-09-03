## Freesound Audio Tagging - COMP47650

COMP47650 - Deep Learning Group Project     

by Daniel Hand, Shane Kennedy and Thu Trang Nguyen.       

## Dataset
1. The contents of [data](https://github.com/DanH139/ucd/tree/master/Deep%20Learning/Freesound%20Audio%20Tagging/data) can be found [here](https://ucd-my.sharepoint.com/:u:/g/personal/daniel_hand_ucdconnect_ie/EZVF_TnCRnpOtuqch0sGDv8B_fPaLB0vkQffHID8LUiWSQ?e=T5g7de)      

2. The audio_test, audio_train and submission files can be downloaded using the following bash commands:     

**audio_train**
``` sh
wget -O FSDKaggle2018.audio_train.zip https://zenodo.org/record/2552860/files/FSDKaggle2018.audio_train.zip?download=1 
```

**audio_test**
``` sh
wget -O FSDKaggle2018.audio_test.zip https://zenodo.org/record/2552860/files/FSDKaggle2018.audio_test.zip?download=1 
```

**submission files**
``` sh 
wget -O FSDKaggle2018.meta.zip https://zenodo.org/record/2552860/files/FSDKaggle2018.meta.zip?download=1 
```


*Alternatively*, 
1. Run [main.py](https://github.com/DanH139/ucd/blob/master/Deep%20Learning/Freesound%20Audio%20Tagging/main.py) which executes the bash commands in [dataset.sh](https://github.com/DanH139/ucd/blob/master/Deep%20Learning/Freesound%20Audio%20Tagging/dataset.sh)

        
            
## Repository Structure
The file structure of this repository* is the following:

FreesoundAudioTagging       
- checkpoints        
   |        
    ----> cnn-weights.{epoch:02d}-{val_loss:.2f}.hdf5       
    ----> vnn-weights.{epoch:02d}-{val_loss:.2f}.hdf5       
- data      
    |              
     ---> audio_test_trim       
        |       
        ===> abcd1234.wav.npy       
     ---> audio_train_trim      
        |       
        ===> abcd1234.wav.npy       
     ---> mel_spec_test     
        |       
        ===> abcd1234.wav.npy       
     ---> mel_spec_train        
        |       
        ===> abcd1234.wav.npy       
dataset.sh      
download_dataset.py     
- figs      
    |       
     ---> cnn-Accuracy_vs_Epochs.png        
     ---> cnn-Loss_vs_Epochs.png        
     ---> vnn-Accuracy_vs_Epochs.png        
     ---> vnn-Loss_vs_Epochs.png        
freesound_audio_tagging.ipynb       
- log       
    |       
     ---> log1.txt      
main.py     
- models        
    |       
    ----> cnn.py        
    ----> sgd.py        
    ----> svm.py        
    ----> vnn.py        
preprocess.py       
requirements.txt        
- submission        
    |       
     ---> test_post_competition_scoring_clips.csv       
     ---> train_post_competition.csv        
README.md       
summary_feats_funcs.py      
visualisation.py        
                                        
6 directories, 52232 files*


*including directories

