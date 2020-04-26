from sklearn.decomposition import PCA
from sklearn.neighbors import KNeighborsRegressor
import pandas as pd
import numpy as np


class Estimator():
    def __init__(self, dataset):
        DF = pd.read_csv(dataset)
        self.X = DF.iloc[:, 0:100].to_numpy()
        self.Y1 = DF.iloc[:, 100].to_numpy()
        self.Y2 = DF.iloc[:, 101].to_numpy()

        if self.X.shape[0] > self.X.shape[1]:
            self.pca = PCA(n_components='mle', svd_solver='full')
        else:
            self.pca = PCA()

        self.neighLatitude = KNeighborsRegressor(n_neighbors=3)
        self.neighLongitude = KNeighborsRegressor(n_neighbors=3)

        self.fit()

    def update(self, dataset):
        DF = pd.read_csv(dataset)
        self.X = DF.iloc[:, 0:100].to_numpy()
        self.Y1 = DF.iloc[:, 100].to_numpy()
        self.Y2 = DF.iloc[:, 101].to_numpy()

        self.fit()
        
        print("Database is updated")

    def estimate(self, x):
        estimatedLatitude = self.neighLatitude.predict(x)[0]
        estimatedLongitude = self.neighLongitude.predict(x)[0]

        return {'latitude': estimatedLatitude, 'longitude': estimatedLongitude}
        
    def fit(self):
        self.pca.fit(self.X)
        self.neighLatitude.fit(self.X, self.Y1)
        self.neighLongitude.fit(self.X, self.Y2)


if __name__ == "__main__":
    print(np.matrix([0, 0, 0]).shape)
