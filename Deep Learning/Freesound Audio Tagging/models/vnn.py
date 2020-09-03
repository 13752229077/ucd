import numpy as np
import tensorflow as tf
from extra_keras_metrics import average_precision_at_k
from keras import Sequential
from keras.callbacks import ModelCheckpoint
from keras.layers import Dense, Dropout
from sklearn.metrics import classification_report
from visualisation import plotNetwork


def fit(x_train, y_train, x_val, y_val):
    # Set the input and output sizes
    input_size = 40  # NUMBER INPUTS HERE#
    output_size = 41  # NUMBER OUTPUTS HERE#

    # DEFINE HIDDEN LAYER SIZE
    # CAN HAVE MULTIPLE DIFFERENT SIZED LAYERS IF NEEDED
    # 50 NICE START POINT FOR BEING TIME EFFICIENT BUT STILL RELATIVELY COMPLEX
    hidden_layer_size = 100

    # MODEL CHECKPOINT
    mc = ModelCheckpoint(
        filepath='checkpoints/vnn-weights.{epoch:02d}-{val_loss:.2f}.hdf5',
        monitor='val_loss',
        verbose=0,
        save_best_only=True,
        mode='max',
        period=5)

    # MODEL SPECIFICATIONS
    model = Sequential([
        # tf.keras.layers.Dense is basically implementing: output = activation(dot(input, weight) + bias)
        # it takes several arguments, but the most important ones for us are the hidden_layer_size and the activation function
        Dense(hidden_layer_size, activation='relu'),  # 1st hidden layer
        Dropout(0.2),
        Dense(hidden_layer_size, activation='relu'),  # 2nd hidden layer
        Dropout(0.2),
        Dense(hidden_layer_size, activation='relu'),  # 3rd hidden layer
        Dropout(0.2),

        # POTENTIALLY MULTIPLE MORE LAYERS HERE #
        # NO SINGLE ACTIVATION NECESSARILY BEST (AT THIS STAGE I DO NOT FULLY UNDERSTAND DIFFERENCES, TRY DIFFERENT VARIATIONs)

        # FINAL LAYER MUST TAKE OUTPUT SIZE
        # FOR CLASSIFICATION PROBLEMS USE SOFTMAX AS ACTIVATION
        Dense(output_size, activation='softmax')  # output layer
    ])

    # COMPILE MODEL GIVING IT OPTIMIZER LOSS FUNCTION AND METRIC OF INTEREST
    # MOST TIMES USE ADAM FOR OPTIMIZER (LOOK AT OTHERS THOUGH)
    # lOSS FUNCTION - MANY DIFFERENT VARIATIONS sparse_categorical_crossentropy IS BASICALLY MIN SUM OF SQUARES
    # TO NOW I AM ONLY INTERESTED IN ACCURACY AT EACH LEVEL (HAVE NOT LOOKED AT OTHER OPTIONS`)

    # model.compile(optimizer='adam', loss='sparse_categorical_crossentropy', metrics=[average_precision_at_k(3), 'accuracy'])
    model.compile(optimizer='adam', loss='sparse_categorical_crossentropy',
                  metrics=['accuracy'])
    ###                            ###
    ###                            ###
    ###          TRAINING          ###
    ###                            ###
    ###                            ###

    # SET SIZE OF BATCHES (FOR SHUFFLING IN PARTS WHEN OVERALL SIZE TO BIG)
    batch_size = 128

    # SET MAXIMUM NUMBER OF EPOCHS (JUST SO DOESNT RUN ENDLESSLY)
    max_epochs = 100

    # SET EARLY STOPPING FUNCTION
    # PATIENCE EQUAL 0 (DEFAULT) => STOPS AS SOON AS FOLLOWING EPOCH HAS REDUCED LOSS
    # PATIENCE EQUAL N => STOPS AFTER N SUBSEQUENT INCREASING LOSSES
    early_stopping = tf.keras.callbacks.EarlyStopping(patience=2)

    ###                            ###
    ###                            ###
    ###         FIT MODEL          ###
    ###                            ###
    ###                            ###

    history = model.fit(x_train,  # train inputs
              y_train,  # train targets
              batch_size=batch_size,  # batch size
              epochs=max_epochs,  # epochs that we will train for (assuming early stopping doesn't kick in)
              callbacks=[early_stopping, mc],  # early stopping
              validation_data=(x_val, y_val),  # validation data
              verbose=1  # shows some information for each epoch so we can analyse
              )
    y_pred = model.predict(x_val)
    y_pred = np.argmax(y_pred, axis=1)
    vanilla_nn = classification_report(y_val, y_pred)

    plotNetwork(history, "vnn")

    return vanilla_nn