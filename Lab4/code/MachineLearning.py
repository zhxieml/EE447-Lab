"""
Machine Learning Model for Predicting Advisor-Advisee Relationship.
"""
import tensorflow as tf
from keras.models import Sequential, Model
from keras.layers import Dense, Activation, Input
from keras.metrics import *
import matplotlib.pyplot as plt
import numpy as np
from keras.utils import np_utils
import pandas as pd
from time import time
from sklearn import metrics
from sklearn.svm import SVC
from sklearn.tree import DecisionTreeClassifier
from sklearn.preprocessing import MinMaxScaler
from datetime import datetime


def one_hot_encode_object_array(arr):
    '''One hot encode a numpy array of objects (e.g. strings)
    '''
    uniques, ids = np.unique(arr, return_inverse=True)
    return np_utils.to_categorical(ids, len(uniques))

def SVM(X_train,y_train,X_test,y_test):
    '''fit a SVM model to the data
    '''
    t0 = time()
    # normalize
    min_max_scaler = MinMaxScaler()
    X_train = min_max_scaler.fit_transform(X_train)
    X_test = min_max_scaler.fit_transform(X_test)

    model = SVC(kernel = "rbf")
    model.fit(X_train, y_train)
    print ("training time:", round(time()-t0, 3), "s")

    # make predictions
    t0 = time()
    expected = y_test
    predicted = model.predict(X_test)
    print ("predicting time:", round(time()-t0, 3), "s")

    # summarize the fit of the model
    score = metrics.accuracy_score(expected, predicted)
    print(score)
    print(metrics.recall_score(expected,predicted))

    return model, score

def DTree(X_train,y_train,X_test,y_test):
    model = DecisionTreeClassifier(min_samples_split = 40)
    t0 = time()
    model.fit(X_train,y_train)
    print ("training time:", round(time()-t0, 3), "s")
    t0 = time()
    expected = y_test
    predicted = model.predict(X_test)
    print ("predicting time:", round(time()-t0, 3), "s")

    # summarize the fit of the model
    score = metrics.accuracy_score(expected, predicted)
    print(score)
    print(metrics.recall_score(expected,predicted))

    return model,score

def simple_deep_learning_model(X_train,y_train,X_test,y_test,X_all,shape_in):
    model = Sequential()
    model.add(Dense(5, input_shape = (shape_in,))) #input scale 
    model.add(Activation('sigmoid'))
    model.add(Dense(2)) #output scale
    model.add(Activation('softmax'))
    t0 = time()
    model.compile(optimizer='adam', loss='categorical_crossentropy', metrics=['accuracy'])

    model.fit(X_train, y_train, nb_epoch=5, batch_size=5, verbose=1)
    print ("training time:", round(time()-t0, 3), "s")
    loss, accuracy = model.evaluate(X_test, y_test, verbose=0)
    print ("Accuracy = {:.5f}".format(accuracy))
    print ("loss = {:.5f}".format(loss))
    t0 = time()
    y_pred_test = model.predict(X_test)
    print ("predicting time:", round(time()-t0, 3), "s")
    y_pred_all = model.predict(X_all)

    return model,y_pred_test,y_pred_all

def predict(model,dataset):
    file_rd = open(dataset,'r')
    
    print("start loading...")
    data = np.loadtxt(file_rd, delimiter=",")[1:,:]
    print("loading finished!")
    
    predicted = model.predict(data)

    return predicted

def print_res(arr,output_file):
    file_wrt = open(output_file,'w')
    for p in arr:
        file_wrt.write(str(p)+'\n')

def cal_set_num(pred_set):
    y_pred_arr_num = []
    for res in pred_set:
        y_pred_arr_num.append(res[1])
    return y_pred_arr_num


if __name__ == "__main__":
    #prepare dataset
    raw_data = open('GroundTruth_and_Features.csv','r')
    # load the CSV file as a numpy matrix
    FeatureAmount = 22
    dataset = np.loadtxt(raw_data, delimiter=",")[:,:FeatureAmount+1]

    # separate the data from the target attributes
    X = dataset[0:,1:]
    y = dataset[0:,0]
    X_train = dataset[0:-100000,1:]

    y_train = dataset[0:-100000,0]
    X_test = dataset[-100000:,1:]
    y_test = dataset[-100000:,0]
    shape_in = X_train.shape[1]
    shape_out = 2

    #construct Machine Learning model
    y_train_ohe = one_hot_encode_object_array(y_train)
    y_test_ohe = one_hot_encode_object_array(y_test)

    # DL
    model_deep_learning, y_pred, y_pred_all = simple_deep_learning_model(X_train,y_train_ohe,X_test,y_test_ohe,X,FeatureAmount)

    # SVM 
    # model_SVM, score_SVM = SVM(X_train, y_train, X_test, y_test)

    # DTree
    # model_DTree, score_DTree = DTree(X_train, y_train, X_test, y_test)
