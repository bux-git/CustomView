#### 自定义属性  
__一.概述__    
自定义View时，如果想通过xml布局中指定一些我们自己需要的参数时，就需要自己定义属性.     

__二.使用自定义属性的步骤__   
* 1.自定义一个CustomView(extends View )类
* 2.在values/attrs文件中定义属性 
* 3.给布局文件中的View添加定义的属性
* 4.在View的类中获取属性值（一般在构造函数中获取）

__1.定义自定义View重写构造方法__

__2.定义自定义属性__     

在attrs文件中:  

    <attr name="testAttr" format="integer"></attr>   自定义属性
使用已有的系统属性或者自定义属性,直接在name中申明使用即可，     

    <declare-styleable name="test">
        <attr name="android:text"></attr>
        <attr name="testAttr">
    </declare-styleable>
    
其中name 表示属性名称 format表示属性值的类型     


format数据类型如下: 

类型  | 说明|
--------- | --------|
reference |资源ID类型，使用时值是资源id，如:android:text="@String/test"|
color |颜色值，注意是颜色的具体ARBG值，而不是颜色的资源ID  如:android:textColor = "#00FF00"|
boolean |布尔值|
dimension |尺寸值 dp为单位|
float |浮点值|
integer |整型值|
string |字符串|
fraction |百分数 android:pivotX = "200%"|      

**enum：枚举值**    
属性定义：   

    <declare-styleable name="名称"> 
          <attr name="orientation"> 
                      <enum name="horizontal" value="0" />
                     <enum name="vertical" value="1" />
         </attr>
    </declare-styleable>

属性使用：   

    <LinearLayout android:orientation = "vertical"></LinearLayout>  
    
注意：枚举类型的属性在使用的过程中只能同时使用其中一个，不能 android:orientation ="horizontal｜vertical"       

**flag：位或运算**  
属性定义：   

    <declare-styleable name="名称"> 
        <attr name="gravity"> 
                  <flag name="top" value="0x30" /> 
                  <flag name="bottom" value="0x50" />
                  <flag name="left" value="0x03" /> 
                  <flag name="right" value="0x05" /> 
                  <flag name="center_vertical" value="0x10" />
        </attr>
    </declare-styleable>
    
属性使用：   

    <TextView android:gravity="bottom|left"/>
    
注意：位运算类型的属性在使用的过程中可以使用多个值     
    
**混合类型**：属性定义时可以指定多种类型值 
属性定义：   

    <declare-styleable name = "名称"> 
            <attr name = "background" format = "reference|color" />
    </declare-styleable>
    
属性使用：   

    <TextView android:text = "@string/test" />
或者：

    <TextView android:text = "测试" />

__四.declare-styleable__         
declare-styleable是表示一系列属性id的数组，在自定义View时由于可以使用多个自定义属性   
可以将自定义View中使用的属性申明到declare-styleable，方便在自定义View中使用      

我们的每定义一个属性都会在R文件中的attr内部类生成一个常量字段：  

    public static final class attr {
        public static final int CustomizeStyle=0x7f010004;
        public static final int attr_one=0x7f010000;
        public static final int attr_two=0x7f010001;
        public static final int attr_three=0x7f010002;
        public static final int attr_four=0x7f010003;
    }

，而declare-styleable则是在R.styleable中生成一个常量数组 及相关属性 

    public static final class styleable {
        public static final int[] Customize = {
            0x7f010000, 0x7f010001, 0x7f010002, 0x7f010003
        };
        public static final int Customize_attr_one = 0;
        public static final int Customize_attr_two = 1;    
        public static final int Customize_attr_three = 2;
        public static final int Customize_attr_four = 3;
    }
    
如上所示:R.styleable.Customize是一个int[]，而里面的元素的值正好和R.attr.attr_one - R.attr.attr_four一一对应，   
而R.styleable.Customize_attr_one等4个值就是R.attr.attr_one-R.attr.attr_four在R.styleable.Customize数组中的索引       
这个数组和索引我们在View中获取自定义View时将会使用到，而我们也可以不使用styleable可以自己在View中构建此数组        

__3.使用属性自定义__         
>1.直接在layout文件中使用

    <com.dqr.www.customattrs.CustomView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Hello World!"
        app:testAttr="1"/>  
     
>2.设置style并在style中设置属性  

    <com.dqr.www.customattrs.CustomView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Hello World!"
        app:testAttr="1"
        style="@style/custom_style"/>

>3.theme中指定在当前Application或Activity中属性的默认值       

     <style name="AppTheme" parent="Theme.AppCompat.Light.DarkActionBar">
            <!-- Customize your theme here. -->
            <item name="colorPrimary">@color/colorPrimary</item>
            <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
            <item name="colorAccent">@color/colorAccent</item>
            <item name="testAttr">1</item>
            <item name="customTheme">@style/custom_style</item>
     </style>
在AppTheme中为属性testAttr指定了值，同时customTheme指定了一个reference，customTheme也是attr中的一个自定义属性.这个里面也为testAttr指定了值        
在theme中为属性设置值的方式有两这种，直接在theme中定义或通过另一个attribute引用一个style    

__4.View中获取自定义属性__      
在View中获取属性值一般都在构造函数中获取      
   
>1.View的构造函数        

       public CustomView(Context context) {
            super(context);
        }
    
        public CustomView(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
        }
    
        public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }
    
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }       
构造函数参数名  | 说明|
--------- | --------|
context | 上下文|
attrs | 布局文件中设置的属性的数组|
defStyleAttr | 这个是当前Theme中的一个attribute，是指向style的一个引用，当在layout xml中和style中都没有为View指定属性时，会从Theme中这个attribute指向的Style中查找相应的属性值，这就是defStyle的意思，如果没有指定属性值，就用这个值，所以是默认值，但这个attribute要在Theme中指定，且是指向一个Style的引用，如果这个参数传入0表示不向Theme中搜索默认值|
defStyleRes|这个也是指向一个Style的资源ID，但是仅在未设置defStyleAttr时起作用|

构造函数的调用:    
第一个构造函数在代码中实例化View会调用，  
第二个构造函数在XML中定义则会调用,xml中设置的属性通过attrs参数传入，    
第三个构造函数系统是不掉用的，要由View显示调用       

>2.获取属性值    
        
     public TypedArray obtainStyledAttributes (AttributeSet set, int[] attrs, int defStyleAttr, int defStyleRes)
     
     TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.testable, defStyleAttr, R.style.defStyleRes); 
     String text=array.getString(R.styleable.testable_testAttr);
     array.recycle();
其中第二个参数attrs，表示我们需要使用的属性的属性ID数组，其他参数和构造函数一行

TypedArray主要有两个作用， 
>第一根据传入的AttributeSet和styleable筛选出我们需要的属性数组  
第二是内部去转换attrid和属性值数组的关系；    
第三是提供了一些类型的自动转化，  

>比如我们getString时，如果你是通过@string/hello这种方式设置的，TypedArray会自动去将ResId对应的string从资源文件中读出来。   
说到底，都是为了方便我们获取属性参数。         

对于一个属性可以在多个地方指定它的值，如XML直接定义，style，Theme，而这些位置定义的值有一个优先级，按优先级从高到低依次是：    


在有使用obtainStyledAttributes第三个参数时:   
直接在布局xml中定义 __>__ 布局xml中引用style __>__ obtainStyledAttributes 第三个参数:defStyleAttr __>__ 直接在theme中定义的属性    
 obtainStyledAttributes最后一个参数 这种情况下设置无效  

在有使用obtainStyledAttributes第三个参数时:   
直接在布局xml中定义 __>__ 布局xml中引用style __>__ obtainStyledAttributes最后一个参数   __>__  直接在theme中定义的属性      

学习资料：   
[深入理解Android 自定义attr Style styleable以及其应用](http://www.jianshu.com/p/61b79e7f88fc#)    
[ Android 深入理解Android中的自定义属性](http://blog.csdn.net/lmj623565791/article/details/45022631/)    
[Android中自定义样式与View的构造函数中的第三个参数defStyle的意义](http://www.cnblogs.com/angeldevil/p/3479431.html)    



