#### 坐标系相关知识

__一.Android坐标系定义__  
Android坐标系是以屏幕左上角为坐标原点，X轴向右增大，Y轴向下增大（与数字坐标系相反）  
在坐标系根据两个点就可以确定一块矩形区域，即View的左上角和右下角2个坐标  
1.相对于View的坐标系:即以View左上角为原点(0,0)的坐标系     
2.相对于父View的坐标系:即以父View左上角为原点(0,0)的坐标系       
3.相对于屏幕坐标系，绝对坐标:即以屏幕左上角为原点(0,0)的坐标系     
总之相对坐标系，就是以某一个View的左上角作为原点的坐标系

-------------
__View相关坐标方法__  

View获取坐标方法  | 坐标说明|
--------- | --------|
以下方法获取坐标都是相对于父布局|
getLeft()  |返回View左上角X坐标 |
getTop()  | 返回View左上角Y坐标 |
getTop()  | 返回View右下角X坐标 |
getTop()  | 返回View右下角Y坐标 |
getX()  | 返回值为getLeft()+getTranslationX()，当setTranslationX()时getLeft()不变，getX()变。 |
getY()  | 返回值为getTop()+getTranslationY()，当setTranslationY()时getTop()不变，getY()变。 |       

__触摸事件相关__  

MotionEvent坐标方法  | 坐标说明|
--------- | --------|
以下方法获取坐标都是相对于父布局|
getX()  |当前触摸点X坐标 |
getY()  | 当前触摸点Y坐标 |
以下方法获取坐标都是相对于屏幕|
getRawX()  | 当前触摸点X坐标 |
getRawY()  | 当前触摸点Y坐标 |

__View宽高相关方法__
    
View宽高方法  | 说明|
--------- | --------|
getWidth()  |layout后有效，返回值是mRight-mLeft，|
getHeight()  | layout后有效，返回值是mBottom-mTop |
getMeasuredWidth()  | 返回measure过程得到的mMeasuredWidth值 |
getMeasuredHeight()  | 返回measure过程得到的mMeasuredHeight值|

__View获取屏幕中位置坐标的相关方法__
    
View的方法  | 说明|
--------- | --------|
getLocalVisibleRect()  |获取相对于View坐标系，View的自身可见的区域的坐标，返回可见区域 左上角和右下角坐标|
getGlobalVisibleRect()  | 获取相对于屏幕坐标系，View的自身可见的区域的坐标，返回可见区域 左上角和右下角坐标|
getLocationOnScreen()  | 获取相对屏幕坐标系,View左上角坐标|
getLocationInWindow()  | 普通Activity 相对屏幕坐标系;如果为对话框式的Activity则是相对于Activity坐标系；返回View的左上角坐标|