# Mining Advisor-advisee Relationships
Not my code.

## Introduction

​	In this lab we focus on learning basic machine learning methods and implementing them on a specific topic, to find advisor-advisee relationships in academic heterogeneous networks. We use **Keras** and **Tensorflow** to build a deep-learning model and compare the performance between deep learning and tradition machine learning methods like **SVM** and **Decision Tree**.

## Dependency

​	Main dependencies are:

- Python 3.6.10
- Tensorflow 2.2.0
- Keras 2.3.1

## Data

​	In this experiment, we have the known ground-truth Advisor-advisee Relationships (AARs) and common coauthor relationships, which are obtained through calculating the probability of being an AAR respectively. The authors are represented by eight 4-bit code.

​	We have extracted features in the perspective of relationship before, in and outside one cooperation. For instance, if we have known A cooperated with B in paper publication from 2008, then the more paper A has before 2008 than B, the more likely that A is Advisor and B is Advisee in their cooperation. We have 22 ordered features ranked through Mutual Information Correlations, method of feature engineering.

​	The features of the known AARs (in file "GroundTruth_and_Features.csv") are used to train and test our machine learning model and we will use the trained model to predict if the common coauthor relationships are AAR or not. 

## Machine Learning Methods

### SVM

​	SVM (support vector machine) is a supervised learning model that analyzes data used for classification and regression analysis. An SVM model is a representation of the examples as points in space, mapped so that the examples of the separate categories are divided by a clear gap (or **margin**) that is as wide as possible.

​	In addition to performing linear classification, SVMs can efficiently perform a non-linear classification using what is called the **kernel trick**, implicitly mapping their inputs into high-dimensional feature spaces.

​	Formally speaking, a hyperplane to separate the sample space can be formulated as:
$$
\boldsymbol{w}^{\mathrm{T}}\boldsymbol{x} + b = 0
$$
​	Moreover, to make restrictions on $\mathbf{w}$, we suppose:
$$
\left\{\begin{array}{ll}
\boldsymbol{w}^{\mathrm{T}} \boldsymbol{x}_{i}+b \geqslant+1, & y_{i}=+1 \\
\boldsymbol{w}^{\mathrm{T}} \boldsymbol{x}_{i}+b \leqslant-1, & y_{i}=-1
\end{array}\right.
$$
​	Then, for those training samples which takes the equality ($\boldsymbol{w}^{\mathrm{T}} \boldsymbol{x}_{i}+b = \pm 1$), we call them **support vectors**. Margin is the sum of distances between the hyperplane and two heterogeneous support vectors:
$$
\gamma=\frac{2}{\|\boldsymbol{w}\|}
$$
​	The problem can therefore be formulated as:
$$
\begin{array}{c}
\max _{\boldsymbol{w}, b} \frac{2}{\|\boldsymbol{w}\|} \\
\text { s.t. } y_{i}\left(\boldsymbol{w}^{\mathrm{T}} \boldsymbol{x}_{i}+b\right) \geqslant 1, \quad i=1,2, \ldots, m
\end{array}
$$
which can be solved using **Lagrange multiplier** methods.

​	Further more, if we take kernels into account, we can map the original sample $\boldsymbol{x}$ into another feature space $\phi(\boldsymbol{x})$. A kernel is represented as:
$$
K\left(\boldsymbol{x}_{i}, \boldsymbol{x}_{j}\right) \stackrel{\text { def }}{=} \phi\left(\boldsymbol{x}_{i}\right)^{\top} \phi\left(\boldsymbol{x}_{j}\right)
$$
​	In practice, **RBF kernel** is very popular, which has the form of:
$$
K\left(\boldsymbol{x}_{i}, \boldsymbol{x}_{j}\right) =\exp \left(-\frac{\left\|\boldsymbol{x}_{i}-\boldsymbol{x}_{j}\right\|^{2}}{2 \sigma^{2}}\right)
$$
where $\sigma$ is the hyper-parameter. **In this experiment, we also use RBF kernel to learn the classification model.**

### Decision Tree

​	Decision Trees are a non-parametric supervised learning method used for classification and regression. The goal is to create a model that predicts the value of a target variable by learning simple decision rules inferred from the data features.

​	More specifically, A decision tree is a flowchart-like structure in which each internal node represents a "test" on an attribute (e.g. whether a coin flip comes up heads or tails), each branch represents the outcome of the test, and each leaf node represents a class label (decision taken after computing all attributes). The paths from root to leaf represent classification rules.

​	The procedure of learning a decision tree basically follows the **divide-and-conquer** strategy, and under three cases, we do not need to split the current node:

- all samples belongs to the same class
- all samples have the exactly same attributes
- there are less samples than `min_samples_split`.  **In this experiment, we set `min_samples_split` to be 40.**

​	The key point for decision trees to work is the `criterion`, which defines how to choose the optimal attribute to split the node. Several criteria are used in practice, including **information entropy**, **Gini index**, and so on. **In this experiment, we use Gini index as the `criterion`**, which quantifies *the possibility that two randomly-selected samples are inconsistent*:
$$
\begin{aligned}
\operatorname{Gini}(D) &=\sum_{k=1}^{|\mathcal{Y}|} \sum_{k^{\prime} \neq k} p_{k} p_{k^{\prime}} \\
&=1-\sum_{k=1}^{|\mathcal{Y}|} p_{k}^{2}
\end{aligned}
$$
where $D$ denotes the dataset.

​	Then, we can define the Gini index over some attribute $a$:
$$
\operatorname{Gini\_index}(D, a)=\sum_{v=1}^{V} \frac{\left|D^{v}\right|}{|D|} \operatorname{Gini}\left(D^{v}\right)
$$
​	Among the candidate attributes $A$ we choose the attribute that minimizes the Gini index:
$$
a_{*}=\arg \min_{a \in A} \operatorname{Gini\_index}(D, a)
$$

## Deep Learning Method

​	Given that deep learning has become so prevalent these years, details are omitted here to save pages. 

​	In this experiment, we build a simple 4-layer network with two convolutional layers and a softmax layer to obtain the prediction result.

## Experiment

### Results

A simple comparison over performance is done among the above three methods. I ran it on my laptop with no GPU supports and solely Intel I5-7300U. 

| Method | Training Time(s) | Predicting Time(s) | Accuracy   |
| ------ | ---------------- | ------------------ | ---------- |
| SVM    | 1.928            | 9.026              | 0.9372     |
| D-Tree | **0.284**        | **0.028**          | **0.9452** |
| DL     | 22.827           | 1.362              | 0.93879    |

Note:

- predicting time and accuracy are calculated over testing samples
- before training the SVM model with RBF kernel, we need to normalize the raw data into $[-1, 1]$ so that the features are distributed under the same scale. The benefits include shorter training time and increase of accuracy. In practice, we use `MinMaxScaler` from scikit-learn to normalize.

### Conclusions

As we may see, although all three models perform fairly well in this task, the decision tree model seems to be the best of them. This reminds us that, despite of concerns on the data, sometimes the simplest model can do the best, as it is "too simple to be wrong". 