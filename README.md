# VerticalScrollView 纵向的文字滚动视图

[ ![VerticalScrollView](https://img.shields.io/badge/VerticalScrollView-1.0.0-orange.svg) ](https://bintray.com/zhangqiaa/maven/VerticalScrollView/_latestVersion)
```
依赖方式  implementation 'com.onion.vertical:VerticalScrollView:1.0.0'
```
```
xml
```
```
<com.onion.vertical.VerticalScrollView
            android:paddingRight="20dp"
            android:id="@+id/main_vertical"
            app:v_yspeed="2"
            app:v_xspeed="1"
            app:v_textSize="12sp"
            app:v_interval="3000"
            app:v_textColor="#ffffff"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:layout_marginLeft="10dp">
        </com.onion.vertical.VerticalScrollView>
```
```
Java
```
```
val data = mutableListOf("梦幻西游今天全新开始了,希望大家来玩!","今天是个好日子!","呵呵","驱动大师大所大所大所多奥术大师大所大所大所大所多奥术大师大所大所大所多奥术大师大
   main_vertical.setData(data)
   main_vertical.start()
```
![image](https://github.com/Onionsss/VerticalScrollView/blob/master/images/bh.gif)
