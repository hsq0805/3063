import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
import sns as sns

from seaborn import jointplot, FacetGrid, pairplot, heatmap

from sklearn.linear_model import LinearRegression
from sklearn.metrics import mean_squared_error
from sklearn.preprocessing import MinMaxScaler
from sklearn.tree import DecisionTreeRegressor
from sklearn.ensemble import AdaBoostRegressor


def main():
    # train data set
    train_data = pd.read_csv("train.csv")
    columns = ["sale_date", "price", "num_bedroom", "num_bathroom", "area_house", "area_parking",
               "floor", "rating", "floorage", "area_basement", "year_built", "year_repair", "latitude", "longitude"]
    train_data = train_data[columns]

    print(train_data.shape)   #查看数据矩阵信息
    print(train_data.describe()) #查看属性信息
    print(train_data.isnull().sum()) # 查看数据中是否有nan值
    print(train_data.info())  # 查看数据的基础信息
    print()
    
    # 各属性与price的相关度
    print(train_data.corr()['price'].sort_values(ascending=False))

    train_data.sale_date = train_data.sale_date.astype('str')  # 将 date数据转化为字符串
    train_data.sale_date = pd.to_datetime(train_data.sale_date)  # date转化为时间序列

    plt.figure(figsize=(8, 5))
    plt.subplot(211)
    # 绘制价格的数据分布曲线
    plt.title('price describution')
    plt.hist(train_data.price, bins=1000)
    # 绘制价格曲线
    plt.subplot(212)
    plt.plot(range(train_data.shape[0]), np.sort(train_data.price.values), 'o')
    plt.grid()

    # 绘制各个特征的分布柱状图
    train_data.hist(figsize=(10, 8), bins=50, grid=False)
    plt.subplots_adjust(hspace=0.8)


    # 连续变量与房价之间的关系
    pairplot(data=train_data, x_vars=['area_house', 'area_parking', 'floorage', 'area_basement'], y_vars="price")
    pairplot(data=train_data, x_vars=['year_built', 'year_repair', 'latitude', 'longitude'], y_vars="price")
    pairplot(data=train_data, x_vars=['rating', 'floor', 'num_bedroom', 'num_bathroom'], y_vars="price")

    continuous_cols = ['area_house', 'area_parking', 'floorage', 'area_basement',
                       'year_built', 'year_repair', 'latitude', 'longitude',
                       'rating', 'floor', 'num_bedroom', 'num_bathroom']

    plt.figure(figsize=(12, 4))
    train_data.corr()['price'][continuous_cols].sort_values(ascending=False).plot(kind='barh')


    # 相关性热值图
    corrmat = train_data.corr()
    plt.subplots(figsize=(10, 10))
    heatmap(corrmat, square=True, cmap='Reds')

    cols = corrmat.nlargest(10, 'price')['price'].index  # 数值最大的前十个
    cm = np.corrcoef(train_data[cols].values.T)
    plt.figure(figsize=(10, 12))
    heatmap(cm, cbar=True, annot=True, square=True, fmt='.2f', cmap='Blues',
                annot_kws={'size': 10}, yticklabels=cols.values, xticklabels=cols.values)

    plt.show()


main()
