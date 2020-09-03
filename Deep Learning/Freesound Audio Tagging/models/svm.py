import pandas as pd

from sklearn.metrics import classification_report, accuracy_score
from sklearn.model_selection import GridSearchCV
from sklearn.svm import SVC


def fit(x_train, y_train, x_val, y_val, encoder, params):
    svm = SVC()
    if params:
        svm.set_params(**params)

    svm.fit(x_train, y_train)
    y_pred = svm.predict(x_val)

    new_index = list(encoder.classes_)
    new_index.append('accuracy')
    new_index.append('macro avg')
    new_index.append('weighted avg')

    report = classification_report(y_val, y_pred, output_dict=True)
    df_svm_first = pd.DataFrame(report).transpose()
    df_svm_first.index = new_index

    return df_svm_first

def grid_search(X, Y, X_test, Y_test):
    svc = SVC()
    parameters = {
        'C': (0.5, 1, 2),
        'kernel': ('rbf', 'linear', 'poly', 'sigmoid'),
        'shrinking': (True, False),
        'decision_function_shape': ('ovp', 'ovr'),

    }
    grid_search = GridSearchCV(svc, parameters, n_jobs=-1, verbose=0)
    grid_search.fit(X, Y)
    accuracy = grid_search.best_score_
    best_parameters = grid_search.best_estimator_.get_params()
    classifier = grid_search.best_estimator_
    y_pred = classifier.predict(X_test)
    test_accuracy = accuracy_score(y_pred, Y_test)


    return best_parameters, accuracy, test_accuracy