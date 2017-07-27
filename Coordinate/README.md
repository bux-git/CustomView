#### 坐标系相关知识


![img](https://raw.githubusercontent.com/bux-git/CustomView/master/Coordinate/coordinate.gif)   

__一.Android坐标系定义__  
Android坐标系是以屏幕左上角为坐标原点，X轴向右增大，Y轴向下增大（与数字坐标系相反）  
在坐标系中根据两个点就可以确定一块矩形区域，即View的左上角和右下角2个坐标可以确定View的大小  
* 1.相对于View的坐标系:即以View左上角为原点(0,0)的坐标系      
* 2.相对于父View的坐标系:即以父View左上角为原点(0,0)的坐标系       
* 3.相对于屏幕坐标系，绝对坐标:即以屏幕左上角为原点(0,0)的坐标系     
* 总之相对坐标系，就是以某一个View的左上角作为原点的坐标系

一般来说，我们要获取 View 的坐标和高度 等，都必须等到 View 绘制完毕以后才能获取的到，   
在 Activity 的 onCreate() 方法 里面 是获取不到的，必须等到 View 绘制完毕以后才能获取地到 View 的响应的坐标，    
一般来说，主要有以下两种方法： 
>    第一种方法，Activity onWindowFocusChanged() 方法里面进行调用

    @Override
     public void onWindowFocusChanged(boolean hasFocus) {
         super.onWindowFocusChanged(hasFocus);
         //确保只执行一次
         if (first && hasFocus) {
             first=true;
         }
     }
>第二种方法，在视图树绘制完成的时候进行测量：

    mMoveView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //执行一次之后移除
                mMoveView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

-------------
__View相关坐标方法__  

View获取坐标方法  | 坐标说明|
--------- | --------|
以下方法获取坐标都是相对于父布局|
getLeft()  |返回View左上角X坐标 |
getTop()  | 返回View左上角Y坐标 |
getTop()  | 返回View右下角X坐标 |
getTop()  | 返回View右下角Y坐标 |
在 api 14 以后 ，在动画执行过程中，要改变 View 的状态，推荐使用 setTranslationY() 和 setTranslationX() 等方法，而尽量避免改变 LayoutParams.因为性能来说较差|
getTranslationX()  | 返回值为X方向上相对与Left的偏移量 默认值为0| 
getTranslationY()  | 返回值为Y方向上相对于top的偏移量 默认值为0| 
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

__View滑动相关的坐标__    

 View的滑动方法  | 说明|
 --------- | --------|
 offsetLeftAndRight(int offset) |X方向挪动View，offset为正则x轴正向移动，移动的是整个View，getLeft()会变的|
 offsetTopAndBottom(int offset)  | Y向挪动View，offset为正则y轴正向移动，移动的是整个View，getTop()会变的 |
  偏移量mScrollX，mScrollY只针对于该View中onDraw()方法里的内容|
   mScrollX | 该View的内容相对于View坐标系X方向的偏移量|
   mScrollY | 该View的内容相对于View坐标系Y方向的偏移量|
 scrollTo(int x, int y)  |将View中的内容在相对于View的坐标系中，滑动偏移相应的x,y 注意：x，y为正时向靠近原点的方向偏移 为负则相反.同时此类滑动方法值改变 mScrollX Y的值 不该变top left等|
scrollBy(int x, int y) | 实质为scrollTo()，只是只改变Y轴滑动。|
setScrollX(int value) | 在scrollTo()的基础上继续滑动xy。|
setScrollY(int value) | 实质为scrollTo()，只是只改变X轴滑动。|
getScrollX()/getScrollY() | 获取当前滑动位置偏移量。|