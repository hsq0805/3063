import numpy as np
import pandas as pd
import seaborn as sns
import matplotlib as mpl
import matplotlib.pyplot as plt
from scipy.stats import norm, probplot, boxcox
from sklearn import preprocessing
from sklearn.linear_model import LinearRegression
from sklearn.metrics import mean_squared_error, r2_score
from sklearn.model_selection import cross_val_score
from sklearn.preprocessing import MinMaxScaler
from sklearn.tree import DecisionTreeRegressor
from sklearn.ensemble import AdaBoostRegressor, RandomForestRegressor
from sklearn.preprocessing import LabelEncoder, OneHotEncoder
from sklearn.preprocessing import StandardScaler
from sklearn.tree import DecisionTreeRegressor
from sklearn.ensemble import RandomForestClassifier

from seaborn.categorical import barplot
from sklearn.utils import stats


def main():
    #train data set
    train_data = pd.read_csv("train.csv")
    train_data = train_data[["sale_date", "price", "num_bedroom", "num_bathroom", "area_house", "area_parking", "floor", "rating", "floorage", "area_basement", "year_built", "year_repair", "latitude", "longitude"]]

    # test data set
    test_data = pd.read_csv("test.csv")
    test_data = test_data[
        ["sale_date", "price", "num_bedroom", "num_bathroom", "area_house", "area_parking", "floor", "rating",
         "floorage", "area_basement", "year_built", "year_repair", "latitude", "longitude"]]


    print(train_data.info())
    # print(train_data['price'].describe())
    # print("Skewness: %f" % train_data['price'].skew())  # 偏度
    # print("Kurtosis: %f" % train_data['price'].kurt())  # 峰度
    # sns.distplot(train_data['price'])

    # data = pd.concat([train_data['price'], train_data['year_built']], axis=1)
    # data.plot.scatter(x='year_built', y='price')

    train_data = train_data.drop(train_data[(train_data['area_house'] > 6000) & (train_data['price'] < 120)].index)
    train_data = train_data.drop(train_data[(train_data['floorage'] > 5000) & (train_data['price'] > 400)].index)
    train_data = train_data.drop(train_data[(train_data['num_bedroom'] > 6) & (train_data['price'] > 500)].index)
    train_data = train_data.drop(train_data[(train_data['area_basement'] < 3000) & (train_data['price'] >400)].index)

    # data = pd.concat([train_data['price'], train_data['floorage']], axis=1)
    # data.plot.scatter(x='floorage', y='price')

    # preprocessing 数据最大—最小规范化
    mms = MinMaxScaler(feature_range=(0, 5))
    # train_data = mms.fit_transform(train_data.values.reshape(-1,1))
    # test_data = mms.fit_transform(test_data.values.reshape(-1,1))
    # train_data["area_house"] = mms.fit_transform(train_data["area_house"].values.reshape(-1, 1))
    # train_data["area_parking"] = mms.fit_transform(train_data["area_parking"].values.reshape(-1, 1))
    # train_data["floorage"] = mms.fit_transform(train_data["floorage"].values.reshape(-1, 1))
    # train_data["year_built"] = mms.fit_transform(train_data["year_built"].values.reshape(-1, 1))
    # test_data["area_house"] = mms.fit_transform(test_data["area_house"].values.reshape(-1, 1))
    # test_data["area_parking"] = mms.fit_transform(test_data["area_parking"].values.reshape(-1, 1))
    # test_data["floorage"] = mms.fit_transform(test_data["floorage"].values.reshape(-1, 1))
    # test_data["year_built"] = mms.fit_transform(test_data["year_built"].values.reshape(-1, 1))


    # 日期分解
    train_data["sale_year"] = pd.to_datetime(train_data["sale_date"], format="%Y%m%d").dt.year
    train_data["sale_month"] = pd.to_datetime(train_data["sale_date"], format="%Y%m%d").dt.month
    train_data["sale_day"] = pd.to_datetime(train_data["sale_date"], format="%Y%m%d").dt.day
    test_data["sale_year"] = pd.to_datetime(test_data["sale_date"], format="%Y%m%d").dt.year
    test_data["sale_month"] = pd.to_datetime(test_data["sale_date"], format="%Y%m%d").dt.month
    test_data["sale_day"] = pd.to_datetime(train_data["sale_date"], format="%Y%m%d").dt.day

    # 构建二值特征：area_basement 和 year_repaired
    train_data['have_basement'] = train_data['area_basement'].apply(lambda x: 1 if x > 0 else 0)
    train_data['is_repaired'] = train_data['year_repair'].apply(lambda x: 1 if x > 0 else 0)
    test_data['have_basement'] = test_data['area_basement'].apply(lambda x: 1 if x > 0 else 0)
    test_data['is_repaired'] = test_data['year_repair'].apply(lambda x: 1 if x > 0 else 0)
    # train_data.drop(columns=['area_basement', 'year_repair'], inplace=True)

    # 对floors、rating、have_basement、is_repaired 拆分属性值,'floor' ,'rating'
    ordinal_cols = ['is_repaired', 'have_basement']
    for col in ordinal_cols:
        dummies = pd.get_dummies(train_data[col], drop_first=False)
        dummies = dummies.add_prefix("{}#".format(col))
        train_data.drop(col, axis=1, inplace=True)
        train_data = train_data.join(dummies)
    for col in ordinal_cols:
        dummies = pd.get_dummies(test_data[col], drop_first=False)
        dummies = dummies.add_prefix("{}#".format(col))
        test_data.drop(col, axis=1, inplace=True)
        test_data = test_data.join(dummies)

    # 房龄
    train_data['building_age'] = train_data["sale_year"] - train_data['year_built']
    test_data['building_age'] = test_data["sale_year"] - test_data['year_built']


    # 卧室数/浴室数 比率
    train_data['num_bedroom'].replace(0, 1, inplace=True)
    train_data['num_bathroom'].replace(0, 1, inplace=True)
    train_data['room_ratio'] = train_data['num_bedroom'] / train_data['num_bathroom']
    test_data['num_bedroom'].replace(0, 1, inplace=True)
    test_data['num_bathroom'].replace(0, 1, inplace=True)
    test_data['room_ratio'] = test_data['num_bedroom'] / test_data['num_bathroom']

    # 房屋面积/建筑面积 比率
    train_data['f_c_ratio'] = train_data['floorage'] / train_data['area_house']
    test_data['f_c_ratio'] = test_data['floorage'] / test_data['area_house']

    # 房屋面积/停车面积 比率
    train_data['f_p_ratio'] = train_data['floorage'] / train_data['area_parking']
    test_data['f_p_ratio'] = test_data['floorage'] / test_data['area_parking']
    # train_data.drop(columns='area_parking', inplace=True)

    # 将建筑年份和销售日期两列删除,'num_bedroom', 'num_bathroom',,  'BuildYearBins'
    #train_data.drop(['year_built', 'sale_year', 'sale_month', 'sale_day', 'year_repair'], axis=1, inplace=True)

    # 标准化数据使符合正态分布
    # train_data["price"] = np.log1p(train_data["price"])  # 对于SalePrice 采用log1p较好---np.expm1(clf1.predict(X_test))
    # test_data["price"] = np.log1p(test_data["price"])
    # plt.hist(train_data.price, bins=1000)
    # print(train_data.info())
    # plt.show()

    # xy decision
    train_X = train_data[["sale_year","sale_month", "num_bedroom", "num_bathroom", "area_house", "area_parking",
                          "floor", "rating", "floorage", "year_built", "latitude", "longitude",
                          'is_repaired#0', 'is_repaired#1', 'have_basement#0', 'have_basement#1',
                          'building_age', 'room_ratio','year_repair', 'f_c_ratio']]
    train_Y = train_data["price"]
    test_X = test_data[["sale_year","sale_month", "num_bedroom", "num_bathroom", "area_house", "area_parking",
                        "floor", "rating", "floorage", "year_built", "latitude", "longitude",
                        'is_repaired#0', 'is_repaired#1', 'have_basement#0', 'have_basement#1',
                        'building_age', 'room_ratio','year_repair', 'f_c_ratio']]
    test_Y = test_data["price"]

    # train_X = train_data[
    #     ['rating', 'latitude', 'area_house', 'building_age', 'num_bathroom', 'f_p_ratio', 'year_built', 'area_parking', 'room_ratio',
    #      'year_repair', 'f_c_ratio', 'longitude', 'num_bedroom', 'is_repaired#0', 'is_repaired#1', 'have_basement#0', 'have_basement#1']]
    # train_Y = train_data["price"]
    # test_X = test_data[
    #     ['rating', 'latitude', 'area_house', 'building_age', 'num_bathroom', 'f_p_ratio', 'year_built', 'area_parking','room_ratio',
    #      'year_repair', 'f_c_ratio', 'longitude', 'num_bedroom', 'is_repaired#0', 'is_repaired#1', 'have_basement#0', 'have_basement#1']]
    # test_Y = test_data["price"]

    print(train_data.info())
    # print(train_data)
#-------------------------------------------------------------------------------------------------

    # 交叉验证进行迭代特征选择：效果不好
    # lm = LinearRegression()
    # features = ['num_bedroom', 'num_bedroom', 'num_bathroom', 'area_house',
    #             'area_parking', 'floor', 'rating', 'floorage', 'area_basement',
    #             'year_built', 'year_repair', 'latitude','longitude','sale_year',
    #             'sale_month', 'sale_day', 'building_age','room_ratio', 'f_c_ratio', 'f_p_ratio']
    # y = train_data['price']
    #
    # selected_features = []
    # rest_features = features[:]
    # best_score = -1e+12
    # '''
    # 交叉检验的评分标准选择'neg_mean_squared_error'，即均方误差，在这里为负数，
    # 绝对值越小表示误差越小，因此初始的best_score设置为一个绝对值很大的负值
    # '''
    # while len(rest_features) > 0:
    #     temp_best_i = ''
    #     temp_best_score = -1e+12
    #     for feature_i in rest_features:
    #         temp_features = selected_features + [feature_i]
    #         X = train_data[temp_features]
    #         scores = cross_val_score(lm, X, y, cv=5, scoring='neg_mean_squared_error')
    #         score = np.mean(scores)
    #         if score > temp_best_score:
    #             temp_best_score = score
    #             temp_best_i = feature_i
    #     print("select", temp_best_i, "acc:", temp_best_score)
    #     if temp_best_score > best_score:
    #         best_score = temp_best_score
    #         selected_features += [temp_best_i]
    #         rest_features.remove(temp_best_i)
    #     else:
    #         break
    # print("best feature set: ", selected_features, "score: ", best_score)

    # linear regression
    print("linear regression result:")
    lr_model = LinearRegression()
    lr_model.fit(train_X, train_Y)
    print("参数:",lr_model.coef_)
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
    print("-------------------------------------------------------------------")

    # randomforest regression
    print("randomforest regression result:")
    rfReg = RandomForestRegressor(n_estimators=100)
    rfReg.fit(train_X, train_Y)
    train_predictY = rfReg.predict(train_X)
    test_predictY = rfReg.predict(test_X)

    print("error on train set:", mean_squared_error(train_predictY, train_Y))
    print("error on test set:", mean_squared_error(test_predictY, test_Y))

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