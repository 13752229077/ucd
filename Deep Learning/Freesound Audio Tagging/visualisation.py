import matplotlib.pyplot as plt
from matplotlib import cm

def categoryVsSample(train):
    df_grouped = train.groupby(['label', 'manually_verified']).count().drop(['freesound_id', 'license'], axis=1)

    cmap = cm.get_cmap('viridis')

    plot = df_grouped.unstack().reindex(df_grouped.unstack().sum(axis=1).sort_values().index) \
        .plot(kind='bar', stacked=True, title="Number of Audio Samples per Category", figsize=(16, 9), colormap=cmap)

    plot.legend(['Unverified', 'Verified']);
    plot.set_xlabel("Category")
    plot.set_ylabel("No. of Samples")

    plot.figure.savefig("figures/Category_vs_No.samples.png", bbox_inches="tight", dpi=100)

def plotNetwork(history, model):

    plt.clf() # clear plot
    # Plot training & validation accuracy values
    plt.plot(history.history['accuracy'])
    plt.plot(history.history['val_accuracy'])
    plt.title('Model accuracy')
    plt.ylabel('Accuracy')
    plt.xlabel('Epoch')
    plt.legend(['Train', 'Test'], loc='upper left')
    plt.savefig("figures/" + model + "-Accuracy_vs_Epochs.png", bbox_inches="tight", dpi=100)
    plt.clf() # clear plot

    # Plot training & validation loss values
    plt.plot(history.history['loss'])
    plt.plot(history.history['val_loss'])
    plt.title('Model loss')
    plt.ylabel('Loss')
    plt.xlabel('Epoch')
    plt.legend(['Train', 'Test'], loc='upper left')

    plt.savefig("figures/" + model + "-Loss_vs_Epochs.png", bbox_inches="tight", dpi=100)