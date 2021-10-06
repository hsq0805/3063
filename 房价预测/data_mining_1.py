import numpy as np
import pandas as pd
import seaborn as sns
import matplotlib as mpl
import matplotlib.pyplot as plt
from sklearn import preprocessing

from sklearn.linear_model import LinearRegression
from sklearn.metrics import mean_squared_error
from sklearn.preprocessing import MinMaxScaler
from sklearn.tree import DecisionTreeRegressor
from sklearn.ensemble import AdaBoostRegressor
from sklearn.preprocessing import LabelEncoder, OneHotEncoder
from sklearn.preprocessing import StandardScaler
from sklearn.tree import DecisionTreeRegressor
from sklearn.ensemble import RandomForestClassifier

from seaborn.categorical import barplot

def main():
    #train data set
    train_data = pd.read_csv("train.csv")
    train_data = train_data[["sale_date", "price", "num_bedroom", "num_bathroom", "area_house", "area_parking", "floor", "rating", "floorage", "area_basement", "year_built", "year_repair", "latitude", "longitude"]]

    train_data["sale_year"] = pd.to_datetime(train_data["sale_date"], format = "%Y%m%d").dt.year
    train_data["sale_month"] = pd.to_datetime(train_data["sale_date"], format = "%Y%m%d").dt.month
    train_data["sale_day"] = pd.to_datetime(train_data["sale_date"], format = "%Y%m%d").dt.day

    

    #test data set
    test_data = pd.read_csv("test.csv")
    test_data = test_data[["sale_date", "price", "num_bedroom", "num_bathroom", "area_house", "area_parking", "floor", "rating", "floorage","area_basement", "year_built","year_repair", "latitude", "longitude"]]

    test_data["sale_year"] = pd.to_datetime(test_data["sale_date"], format = "%Y%m%d").dt.year
    test_data["sale_month"] = pd.to_datetime(test_data["sale_date"], format = "%Y%m%d").dt.month
    test_data["sale_day"] = pd.to_datetime(train_data["sale_date"], format = "%Y%m%d").dt.day

    #preprocessing
    mms =  MinMaxScaler(feature_range=(0,5))

    # train_data = mms.fit_transform(train_data.values.reshape(-1,1))
    # test_data = mms.fit_transform(test_data.values.reshape(-1,1))
    train_data["area_house"] = mms.fit_transform(train_data["area_house"].values.reshape(-1,1))
    train_data["area_parking"] = mms.fit_transform(train_data["area_parking"].values.reshape(-1,1))
    train_data["floorage"] = mms.fit_transform(train_data["floorage"].values.reshape(-1,1))
    train_data["year_built"] = mms.fit_transform(train_data["year_built"].values.reshape(-1,1))

    test_data["area_house"] = mms.fit_transform(test_data["area_house"].values.reshape(-1,1))
    test_data["area_parking"] = mms.fit_transform(test_data["area_parking"].values.reshape(-1,1))
    test_data["floorage"] = mms.fit_transform(test_data["floorage"].values.reshape(-1,1))
    test_data["year_built"] = mms.fit_transform(test_data["year_built"].values.reshape(-1,1))

    # ss = StandardScaler()
    # train_data = ss.fit_transform(train_data)
    # test_data = ss.fit_transform(test_data)


    #xy decision
    train_X = train_data[["sale_year","sale_month", "num_bedroom", "num_bathroom", "area_house", "area_parking", "floor", "rating", "floorage", "year_built", "latitude", "longitude"]]
    train_Y = train_data["price"]
    test_X = test_data[["sale_year","sale_month", "num_bedroom", "num_bathroom", "area_house", "area_parking", "floor", "rating", "floorage", "year_built",  "latitude", "longitude"]]
    test_Y = test_data["price"]


    print(train_data)
#-------------------------------------------------------------------------------------------------

    #linear regression
    print("linear regression result:")
    lr_model = LinearRegression()
    lr_model.fit(train_X,train_Y)
    train_predictY = lr_model.predict(train_X)
    test_predictY = lr_model.predict(test_X)

    print("error on train set:", mean_squared_error(train_predictY,train_Y))
    print("error on test set:", mean_squared_error(test_predictY,test_Y))
    print("-------------------------------------------------------------------")

    #regression tree
    print("regression tree result:")
    rt_model = DecisionTreeRegressor(random_state=0)
    rt_model.fit(train_X,train_Y)
    train_predictY = rt_model.predict(train_X)
    test_predictY = rt_model.predict(test_X)

    print("error on train set:", mean_squared_error(train_predictY,train_Y))
    print("error on test set:", mean_squared_error(test_predictY,test_Y))
    print("-------------------------------------------------------------------")

    #adaboost  regression
    print("adaboost regression result:")
    adbst_model = AdaBoostRegressor(DecisionTreeRegressor(max_depth = 6 ), n_estimators = 25, learning_rate = 0.6)
    adbst_model.fit(train_X,train_Y)
    train_predictY = adbst_model.predict(train_X)
    test_predictY = adbst_model.predict(test_X)

    print("error on train set:", mean_squared_error(train_predictY,train_Y))
    print("error on test set:", mean_squared_error(test_predictY,test_Y))

    # #visualization
    # sns.set(color_codes=True)
    # np.random.seed(sum(map(ord, "regression")))
    # train_data.head()
    # plt.show()
    # # sns.regplot(x="num_bedroom", y="price", data=train_data) 
    # sns.regplot(x="num_bathroom", y="price", data=train_data) 
    # sns.regplot(x="area_basement", y="price", data=train_data) 

    # plt.scatter(train_data["area_house"], train_data["price"]) # 价格与area_house关系
    # plt.show()
    # plt.show()

    # # sns.set()
    # # sns.set_theme(style="darkgrid")
    # # train_data["price"].hist(bins = 30)
    # # plt.show()
    # # sns.set()
    # # sns.barplot(x = "sale_year", y = "price", data = train_data) 
    # # plt.show()


main()