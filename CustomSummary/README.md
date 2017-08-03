#### 自定义View总览  
总结思路:先总结出自定义 View的主要流程，然后在根据每一个流程去详细学习总结    
相应流程需要的知识点  

__学习资料__    
[ Android 自定义View (一)](http://blog.csdn.net/lmj623565791/article/details/24252901)  
[Android 手把手教您自定义ViewGroup（一）](http://blog.csdn.net/lmj623565791/article/details/38339817)  
[自定义控件三部曲视图篇（一）——测量与布局](http://blog.csdn.net/harvic880925/article/details/47029169)     
[自定义View分类与流程](https://github.com/GcsSloop/AndroidNote/blob/master/CustomView/Advance/%5B01%5DCustomViewProcess.md)     

__一.总述__    
* 1.ViewGroup的职责    

>ViewGroup相当于一个放置View的容器，并且我们在写布局XML的时候，会告诉容器   
(凡是以layout为开头的属性，都是为用于告诉容器ViewGroup的)，我们的宽度(layout_width)   
高度(layout_height),对齐方式(layout_gravity)等，凡是带有layout开头的属性都是给ViewGroup 
使用;至此,ViewGroup的职能为:
>>1.给childView计算出建议的高度和测量模式（onMeasure）；   
为什么只是建议的宽和高，而不是直接确定呢，别忘了childView宽和高可以设置为wrap_content，这样只有childView才能计算出自己的宽和高。   
2.决定childView的位置(onLayout)；  
 
* 2.View的职责 

>1.根据测量模式和ViewGroup给出的建议宽高，计算出自己的宽高；  
2.在ViewGroup为其指定的区域内绘制自己的形态     

* 3.ViewGroup和LayoutParams之间的关系  

>
1.LayoutParams相当于一个布局的信息包，它封装了Layout的位置、高、宽等信息  
2.LayoutParams只是ViewGroup的一个内部类    
3.不同的ViewGroup有不同的LayoutParams内部类,不过他们的都继承与ViewGroup    
4.LayoutParam是由childView携带，在ViewGroup中去使用 如测量 布局时使用     

__二.自定义View步骤__     

* 1、继承View或者ViewGroup或者已有的View和ViewGroup如:TextView.LinearLayout等
* 2、自定义View的属性    
* 3、在View的构造方法中获得我们自定义的属性     
* 4、重写onMeasure() 测量View大小 
* 5、重写onLayout()  确定子View布局(自定义ViewGroup包含子View时有用)
* 6、重写onDraw       实际绘制内容



 


