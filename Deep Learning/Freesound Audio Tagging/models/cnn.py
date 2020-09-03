
import numpy as np
import tensorflow as tf
from keras import Sequential
from keras.callbacks import ModelCheckpoint
from keras.layers import Conv1D, Dropout, GlobalMaxPooling1D, Dense
from keras.wrappers.scikit_learn import KerasClassifier
from sklearn.metrics import classification_report

from visualisation import plotNetwork


def fit(x_train, y_train, x_val, y_val):
    x_train = np.expand_dims(x_train, axis=-1)
    x_val = np.expand_dims(x_val, axis=-1)

    x_train = x_train.astype(float)
    x_val = x_val.astype(float)

    #Set the input and output sizes
    input_size = 40 #NUMBER INPUTS HERE#
    output_size = 41 #NUMBER OUTPUTS HERE#

    def create_model(batch_size = 128, optimizer = 'Adam' ,dropout_rate=0.2,neurons=100,activation='relu', kernel = 3, filters = 500):

        model = Sequential()
        model.add(Conv1D(1000,
                     40,
                     padding='valid',
                     activation=activation,
                     strides=5))
        model.add(Dropout(dropout_rate))
        model.add(GlobalMaxPooling1D())
        model.add(Dense(neurons, activation=activation))
        model.add(Dropout(dropout_rate))
        model.add(Dense(output_size, activation='softmax'))
        model.compile(loss = "sparse_categorical_crossentropy", optimizer = optimizer, metrics = ["accuracy"])
        return model

    early_stopping = tf.keras.callbacks.EarlyStopping(patience=3, restore_best_weights =True)
    # MODEL CHECKPOINT
    mc = ModelCheckpoint(
        filepath='checkpoints/cnn-weights.{epoch:02d}-{val_loss:.2f}.hdf5',
        monitor='val_loss',
        verbose=0,
        save_best_only=True,
        mode='max',
        period=5)


    model = KerasClassifier(build_fn=create_model)

    history = model.fit(x_train, # train inputs
              y_train, # train targets
              batch_size=128, # batch size
              epochs=100, # epochs that we will train for (assuming early stopping doesn't kick in)
              callbacks=[early_stopping, mc], # early stopping
              validation_data=(x_val, y_val), # validation data
              verbose = 1 # shows some information for each epoch so we can analyse
              )

    y_pred = model.predict(x_val)
    # y_pred = np.argmax(y_pred, axis=1)
    cnn = classification_report(y_val, y_pred)

    plotNetwork(history, "cnn")

    return cnn