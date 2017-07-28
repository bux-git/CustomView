#### 自定义属性  
__一.概述__    
自定义View时，如果想通过xml布局中指定一些我们自己需要的参数时，就需要自己定义属性.     

__二.使用自定义属性的步骤__   
* 1.在attrs文件中定义属性 
* 2.给布局文件中的View添加定义的属性
* 3.在View的类中获取属性值（一般在构造函数中获取）


__定义自定义属性__     

在attrs文件中:  

    <attr name="test" format="integer"></attr>  
    
其中name 表示属性名称 format表示属性接收的值的类型     
数据类型如下: 

类型  | 说明|
--------- | --------|
reference |资源ID类型，使用时值是资源id，如:android:text="@String/test"|
color |颜色值，注意是颜色的具体ARBG值，而不是颜色的资源ID  android:textColor = "#00FF00"|
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

