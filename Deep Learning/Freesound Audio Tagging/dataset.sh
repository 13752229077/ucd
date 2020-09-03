wget -O FSDKaggle2018.audio_train.zip https://zenodo.org/record/2552860/files/FSDKaggle2018.audio_train.zip?download=1
wget -O FSDKaggle2018.audio_test.zip https://zenodo.org/record/2552860/files/FSDKaggle2018.audio_test.zip?download=1
wget -O FSDKaggle2018.meta.zip https://zenodo.org/record/2552860/files/FSDKaggle2018.meta.zip?download=1
unzip ./*.zip
mkdir -p data submission figures checkpoints
mv FSDKaggle2018.audio_train data/train
mv FSDKaggle2018.audio_test data/test
mv FSDKaggle2018.meta/test_post_competition_scoring_clips.csv FSDKaggle2018.meta/train_post_competition.csv submission/
rm FSDKaggle2018.audio_train.zip FSDKaggle2018.audio_test.zip FSDKaggle2018.meta.zip
rm -R FSDKaggle2018.meta
pip install -r requirements.txt
