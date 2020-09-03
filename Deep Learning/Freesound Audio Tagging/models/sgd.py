import pandas as pd

from sklearn.linear_model import SGDClassifier
from sklearn.metrics import classification_report

def fit(x_train, y_train, x_val, y_val, encoder):
    SGD = SGDClassifier()
    SGD.fit(x_train, y_train)
    y_pred = SGD.predict(x_val)

    new_index = list(encoder.classes_)
    new_index.append('accuracy')
    new_index.append('macro avg')
    new_index.append('weighted avg')

    report = classification_report(y_val, y_pred, output_dict=True)
    df_sgd_first = pd.DataFrame(report).transpose()
    df_sgd_first.index = new_index

    return df_sgd_first