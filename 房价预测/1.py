import numpy as np
import pandas as pd
from sklearn.linear_model import LinearRegression
from sklearn.metrics import mean_squared_error
from sklearn.ensemble import AdaBoostRegressor




def main():

    columns = ["sale_date", "price", "num_bedroom", "num_bathroom", "area_house", "area_parking",
                             "floor", "rating", "floorage", "area_basement", "year_built", "year_repair", "latitude", "longitude"]

    # train data set
    all_data = pd.read_csv("all.csv")
    all_data = all_data[columns]
    print(all_data.shape)  # 查看数据矩阵信息


    # 年份 季度 月份
    all_data["sale_year"] = pd.to_datetime(all_data["sale_date"], format="%Y%m%d").dt.year
    all_data["sale_month"] = pd.to_datetime(all_data["sale_date"], format="%Y%m%d").dt.month
    all_data["sale_day"] = pd.to_datetime(all_data["sale_date"], format="%Y%m%d").dt.day
    all_data.drop(columns='sale_date', inplace=True)

    # 建筑年份从1900年到2015年，每隔15年分一组
    temp_list = [i for i in range(1900, 2021)]
    bins = temp_list[::15]
    all_data['BuildYearBins'] = pd.cut(all_data['year_built'], bins=bins)

    # 使用pandas的get_dummies方法进行one-hot编码，得到虚拟变量，添加前缀‘Year’
    BuildingYearBinsDf = pd.get_dummies(all_data['BuildYearBins'], prefix='Year')

    # 添加虚拟变量到数据集中
    all_data = pd.concat([all_data, BuildingYearBinsDf], axis=1)
    SaleDateMonthDf = pd.get_dummies(all_data['sale_month'], prefix='Month')
    all_data = pd.concat([all_data, SaleDateMonthDf], axis=1)

    # 房龄
    all_data['building_age'] = all_data["sale_year"] - all_data['year_built']


    # 构建二值特征：area_basement 和 year_repaired
    all_data['have_basement'] = all_data['area_basement'].apply(lambda x: 1 if x > 0 else 0)
    all_data['is_repaired'] = all_data['year_repair'].apply(lambda x: 1 if x > 0 else 0)
    # all_data.drop(columns=['area_basement', 'year_repair'], inplace=True)


    # 对floors、rating、have_basement、is_repaired 拆分属性值
    ordinal_cols = ['is_repaired', 'have_basement','floor' ,'rating']
    for col in ordinal_cols:
        dummies = pd.get_dummies(all_data[col], drop_first=False)
        dummies = dummies.add_prefix("{}#".format(col))
        all_data.drop(col, axis=1, inplace=True)
        all_data = all_data.join(dummies)


    # 卧室数/浴室数 比率
    all_data['num_bedroom'].replace(0, 1, inplace=True)
    all_data['num_bathroom'].replace(0, 1, inplace=True)
    all_data['room_ratio'] = all_data['num_bedroom'] / all_data['num_bathroom']

    # 房屋面积/建筑面积 比率
    all_data['f_c_ratio'] = all_data['floorage'] / all_data['area_house']

    # 房屋面积/停车面积 比率
    all_data['f_p_ratio'] = all_data['floorage'] / all_data['area_parking']
    # all_data.drop(columns='area_parking', inplace=True)

    # 将建筑年份和销售日期两列删除,
    all_data.drop(['year_built', 'sale_year', 'sale_month', 'sale_day', 'year_repair', 'num_bedroom', 'num_bathroom', 'BuildYearBins'], axis=1, inplace=True)

    # print(all_data.info())

    # 训练集  测试集划分
    train_data = all_data[all_data.index < 8000]
    test_data = all_data[all_data.index >= 8000]

    train_X = train_data.drop("price", axis=1, inplace=False)
    train_Y = train_data["price"]
    test_X = test_data.drop('price', axis=1, inplace=False)
    test_Y = test_data["price"]

    print(train_X.info())

    from sklearn.decomposition import PCA
    df = pd.concat([train_X], axis=1)
    model = PCA(n_components=5)
    model.fit_transform(df)
    exp_var = np.round(model.explained_variance_ratio_, decimals=5)
    print('各个主成分解释的方差百分比', exp_var)


    # linear regression
    print("linear regression result:")
    lr_model = LinearRegression()
    lr_model.fit(train_X, train_Y)
    train_predictY = lr_model.predict(train_X)
    test_predictY = lr_model.predict(test_X)

    print("error on train set:", mean_squared_error(train_predictY, train_Y))
    print("error on test set:", mean_squared_error(test_predictY, test_Y))

    # adaboost  regression
    print("adaboost regression result:")
    from sklearn.tree import DecisionTreeRegressor
    adbst_model = AdaBoostRegressor(DecisionTreeRegressor(max_depth=6),n_estimators=30 ,learning_rate=0.5)
    adbst_model.fit(train_X, train_Y)
    train_predictY = adbst_model.predict(train_X)
    test_predictY = adbst_model.predict(test_X)

    print("error on train set:", mean_squared_error(train_predictY, train_Y))
    print("error on test set:", mean_squared_error(test_predictY, test_Y))

main()