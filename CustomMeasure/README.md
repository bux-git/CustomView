#### View 测量    
       
__学习资料:__       
[ 自定义控件其实很简单7/12](http://blog.csdn.net/aigestudio/article/details/42989325)    
[自定义View系列教程02--onMeasure源码详尽分析](http://blog.csdn.net/lfdfhl/article/details/51347818)    
[Android 手把手教您自定义ViewGroup（一）](http://blog.csdn.net/lmj623565791/article/details/38339817)    


__一.MeasureSpec__   
View的测量过程中使用MeasureSpec在子View与父View之间传递测量结果。    
MeasureSpec简介：  
>1 MeasureSpec封装了父布局传递给子View的布局要求。   
 2 MeasureSpec可以表示宽和高    
 3 MeasureSpec由size和mode组成      
 
它是一个32位的int数据. 其中高2位代表SpecMode即某种测量模式，低30位为SpecSize代表在该模式下  
的规格大小     
可以通过如下方式分别获取这两个值：
获取SpecSize

    int specSize = MeasureSpec.getSize(measureSpec)
获取specMode

    int specMode = MeasureSpec.getMode(measureSpec)


当然，也可以通过这两个值生成新的MeasureSpec

    int measureSpec=MeasureSpec.makeMeasureSpec(size, mode);        

SpecMode一共有三种模式:    

模式  | 说明|
--------- | --------|
MeasureSpec.EXACTLY   | 父容器已经检测出子View所需要的精确大小。在该模式下，View的测量大小即为SpecSize |
MeasureSpec.AT_MOST   | 父容器未能检测出子View所需要的精确大小，但是指定了一个可用大小即specSize 在该模式下，View的测量大小不能超过SpecSize |
MeasureSpec.UNSPECIFIED    | 这种模式一般用作Android系统内部，或者ListView和ScrollView等滑动控件|        

__二.测量__       

View 提供给我们操作控件测量的方法是onMeasure:  

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
                getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
    }       

onMeasure的2个签名参数是由View的父View传进来的宽高的测量规格，那么根据布局层级往上 这个测量规格最开始是由根布局传入.   
但是在我们的布局文件中的最外层布局其实并不是Activity中的根布局     
[Android界面架构(Activity,PhoneWiondow,DecorView)简介](http://www.cnblogs.com/l2rf/p/6099170.html)       
![image](activity_decorview.jpg)
由上可知在Activity中我们应用窗口的根View 都为DecorView,确定他最初的值是在ViewRootImpl中      

    private void performTraversals() {  
        // ………………  
      
        if (!mStopped) {  
            // ……
      
            int childWidthMeasureSpec = getRootMeasureSpec(mWidth, lp.width);  
            int childHeightMeasureSpec = getRootMeasureSpec(mHeight, lp.height);  
      
            // ……
      
            performMeasure(childWidthMeasureSpec, childHeightMeasureSpec);  
        }  
      
        // ………………  
    } 

    private static int getRootMeasureSpec(int windowSize, int rootDimension) {  
        int measureSpec;  
        switch (rootDimension) {  
      
        case ViewGroup.LayoutParams.MATCH_PARENT:  
            // Window不能调整其大小，强制使根视图大小与Window一致  
            measureSpec = MeasureSpec.makeMeasureSpec(windowSize, MeasureSpec.EXACTLY);  
            break;  
        case ViewGroup.LayoutParams.WRAP_CONTENT:  
            // Window可以调整其大小，为根视图设置一个最大值  
            measureSpec = MeasureSpec.makeMeasureSpec(windowSize, MeasureSpec.AT_MOST);  
            break;  
        default:  
            // Window想要一个确定的尺寸，强制将根视图的尺寸作为其尺寸  
            measureSpec = MeasureSpec.makeMeasureSpec(rootDimension, MeasureSpec.EXACTLY);  
            break;  
        }  
        return measureSpec;  
    }  
    
        private void performMeasure(int childWidthMeasureSpec, int childHeightMeasureSpec) {
            Trace.traceBegin(Trace.TRACE_TAG_VIEW, "measure");
            try {
                mView.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            } finally {
                Trace.traceEnd(Trace.TRACE_TAG_VIEW);
            }
        }
        
所以我们布局中的测量规格最初是由这里确定，然后这个规格会被由上至下传递下去，并由当前view与其父容器共同作用决定最终的测量大小     
ViewGroup,View递归调用实现测量的过程中有几个重要的方法:     

ViewGroup和普通控件都继承了View,他们的测量都是从View.measure(int widthMeasureSpec, int heightMeasureSpec)方法开始    

    public final void measure(int widthMeasureSpec, int heightMeasureSpec) {  
        // 省略部分代码……  
      
        /* 
         * 判断当前mPrivateFlags是否带有PFLAG_FORCE_LAYOUT强制布局标记 
         * 判断当前widthMeasureSpec和heightMeasureSpec是否发生了改变 
         */  
        if ((mPrivateFlags & PFLAG_FORCE_LAYOUT) == PFLAG_FORCE_LAYOUT ||  
                widthMeasureSpec != mOldWidthMeasureSpec ||  
                heightMeasureSpec != mOldHeightMeasureSpec) {  
      
            // 如果发生了改变表示需要重新进行测量此时清除掉mPrivateFlags中已测量的标识位PFLAG_MEASURED_DIMENSION_SET  
            mPrivateFlags &= ~PFLAG_MEASURED_DIMENSION_SET;  
      
            resolveRtlPropertiesIfNeeded();  
      
            int cacheIndex = (mPrivateFlags & PFLAG_FORCE_LAYOUT) == PFLAG_FORCE_LAYOUT ? -1 :  
                    mMeasureCache.indexOfKey(key);  
            if (cacheIndex < 0 || sIgnoreMeasureCache) {  
                // 测量View的尺寸  
                onMeasure(widthMeasureSpec, heightMeasureSpec);  
                mPrivateFlags3 &= ~PFLAG3_MEASURE_NEEDED_BEFORE_LAYOUT;  
            } else {  
                long value = mMeasureCache.valueAt(cacheIndex);  
      
                setMeasuredDimension((int) (value >> 32), (int) value);  
                mPrivateFlags3 |= PFLAG3_MEASURE_NEEDED_BEFORE_LAYOUT;  
            }  
      
            /* 
             * 如果mPrivateFlags里没有表示已测量的标识位PFLAG_MEASURED_DIMENSION_SET则会抛出异常 
             */  
            if ((mPrivateFlags & PFLAG_MEASURED_DIMENSION_SET) != PFLAG_MEASURED_DIMENSION_SET) {  
                throw new IllegalStateException("onMeasure() did not set the"  
                        + " measured dimension by calling"  
                        + " setMeasuredDimension()");  
            }  
      
            // 如果已测量View那么就可以往mPrivateFlags添加标识位PFLAG_LAYOUT_REQUIRED表示可以进行布局了  
            mPrivateFlags |= PFLAG_LAYOUT_REQUIRED;  
        }  
      
        // 最后存储测量完成的测量规格  
        mOldWidthMeasureSpec = widthMeasureSpec;  
        mOldHeightMeasureSpec = heightMeasureSpec;  
      
        mMeasureCache.put(key, ((long) mMeasuredWidth) << 32 |  
                (long) mMeasuredHeight & 0xffffffffL); // suppress sign extension  
    }  


在View.measure方法中会调用View.onMeasure()方法，此方法在我们自定义View，ViewGroup时，可以重写来进行对View的测量      
我们查看View的默认实现:

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {  
        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),  
                getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));  
    }  

其直接调用了setMeasuredDimension方法为其设置了两个计算后的测量值：

    protected final void setMeasuredDimension(int measuredWidth, int measuredHeight) {  
        // 省去部分代码……  
      
        // 设置测量后的宽高  
        mMeasuredWidth = measuredWidth;  
        mMeasuredHeight = measuredHeight;  
      
        // 重新将已测量标识位存入mPrivateFlags标识测量的完成  
        mPrivateFlags |= PFLAG_MEASURED_DIMENSION_SET;  
}  
   
回到onMeasure方法，我们来看看这两个测量值具体是怎么获得的，其实非常简单，首先来看getSuggestedMinimumWidth方法：    

    protected int getSuggestedMinimumWidth() {  
        return (mBackground == null) ? mMinWidth : max(mMinWidth, mBackground.getMinimumWidth());  
    }   
    
如果背景为空那么我们直接返回mMinWidth最小宽度否则就在mMinWidth和背景最小宽度之间取一个最大值，getSuggestedMinimumHeight类同，        

    public static int getDefaultSize(int size, int measureSpec) {  
        // 将我们获得的最小值赋给result  
        int result = size;  
      
        // 从measureSpec中解算出测量规格的模式和尺寸  
        int specMode = MeasureSpec.getMode(measureSpec);  
        int specSize = MeasureSpec.getSize(measureSpec);  
      
        /* 
         * 根据测量规格模式确定最终的测量尺寸 
         */  
        switch (specMode) {  
        case MeasureSpec.UNSPECIFIED:  
            result = size;  
            break;  
        case MeasureSpec.AT_MOST:  
        case MeasureSpec.EXACTLY:  
            result = specSize;  
            break;  
        }  
        return result;  
    }  

上述onMeasure的过程则是View默认的处理过程,我们自定义时可以重写onMeasure实现自己的测量逻辑        
 

__自定义View ,ViewGroup重写onMeasure()__   

由于MeasureSpec.AT_MOST，MeasureSpec.EXACTLY 模式下解算出的宽高都是等于父View的剩余宽高，  
所以在View默认情况下不管是math_parent还是warp_content都能占满父容器的剩余控件，所以一般在自定义时， 
需要在onMeasure()中处理MeasureSpec.AT_MOST的情况 去算出控件的实际宽高，类似与下面这种模式:    

    @Override  
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {  
        // 声明一个临时变量来存储计算出的测量值  
        int resultWidth = 0;  
      
        // 获取宽度测量规格中的mode  
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);  
      
        // 获取宽度测量规格中的size  
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);  
      
        /* 
         * 如果父View准确测量出子View的大小
         */  
        if (modeWidth == MeasureSpec.EXACTLY) {  
            // 子View直接使用
            resultWidth = sizeWidth;  
        }  
        else {  
            // 子View自己计算出大小  
            resultWidth = ......;  
      
            /* 
             * 如果是AT_MOST模式，子View宽度不应超过父View给出的现在
             */  
            if (modeWidth == MeasureSpec.AT_MOST) {  
               //得到最大限制和计算出的宽之间的最小值
                resultWidth = Math.min(resultWidth, sizeWidth);  
            }  
        }  
      
        int resultHeight = 0;  
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);  
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);  
      
        if (modeHeight == MeasureSpec.EXACTLY) {  
            resultHeight = sizeHeight;  
        } else {  
            resultHeight = .....;  
            if (modeHeight == MeasureSpec.AT_MOST) {  
                resultHeight = Math.min(resultHeight, sizeHeight);  
            }  
        }  
      
        // 设置测量尺寸  
        setMeasuredDimension(resultWidth, resultHeight);  
    } 

在自定义ViewGroup中重写onMeasure()我们除了在测量模式为MeasureSpec.AT_MOST情况下，计算出自己的宽高外     
还需要计算出子控件的宽高，ViewGroup中一般计算子View测量宽高的方法有以下几种        
measureChildren、measureChild和measureChildWithMargins还有getChildMeasureSpec 几个方法，     
我们在自定义ViewGroup时可以使用这些方法去计算
  
      /**
       * 通过父容器传入的widthMeasureSpec和heightMeasureSpec遍历子元素并调用measureChild方法去测量每一个子元素的宽高
       *
       * @param widthMeasureSpec 宽测量模式
       * @param heightMeasureSpec 高测试模式
       */
      protected void measureChildren(int widthMeasureSpec, int heightMeasureSpec) {
          final int size = mChildrenCount;
          final View[] children = mChildren;
          for (int i = 0; i < size; ++i) {
              final View child = children[i];
              if ((child.mViewFlags & VISIBILITY_MASK) != GONE) {
                  measureChild(child, widthMeasureSpec, heightMeasureSpec);
              }
          }
      }
  
      /**
       * 根据ViewGroup传入的测量模式，计算出子View的建议测量模式
        有处理padding
       *
       * @param child The child to measure
       * @param parentWidthMeasureSpec The width requirements for this view
       * @param parentHeightMeasureSpec The height requirements for this view
       */
      protected void measureChild(View child, int parentWidthMeasureSpec,
              int parentHeightMeasureSpec) {
          final LayoutParams lp = child.getLayoutParams();
  
          final int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec,
                  mPaddingLeft + mPaddingRight, lp.width);
          final int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec,
                  mPaddingTop + mPaddingBottom, lp.height);
  
          child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
      }

    /**
     * 根据ViewGroup传入的测量模式，计算出子View的建议测量模式
               有处理padding和子View的Margin
     *
     * @param child The child to measure
     * @param parentWidthMeasureSpec The width requirements for this view
     * @param widthUsed Extra space that has been used up by the parent
     *        horizontally (possibly by other children of the parent)
     * @param parentHeightMeasureSpec The height requirements for this view
     * @param heightUsed Extra space that has been used up by the parent
     *        vertically (possibly by other children of the parent)
     */
    protected void measureChildWithMargins(View child,
            int parentWidthMeasureSpec, int widthUsed,
            int parentHeightMeasureSpec, int heightUsed) {
        final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

        final int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec,
                mPaddingLeft + mPaddingRight + lp.leftMargin + lp.rightMargin
                        + widthUsed, lp.width);
        final int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec,
                mPaddingTop + mPaddingBottom + lp.topMargin + lp.bottomMargin
                        + heightUsed, lp.height);

        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }
        
        /**
         *spec 父View的宽高测量模式
         *padding 已经占用的空间
         *childDimension 子View XML中指定的宽高 其中LayoutParams.MATCH_PARENT=-1
         *LayoutParams.WRAP_CONTENT=-2
         **/
        public static int getChildMeasureSpec(int spec, int padding, int childDimension) {  
            // 获取父容器的测量模式和尺寸大小  
            int specMode = MeasureSpec.getMode(spec);  
            int specSize = MeasureSpec.getSize(spec);  
          
            // 这个尺寸应该减去内边距的值  
            int size = Math.max(0, specSize - padding);  
          
            // 声明临时变量存值  
            int resultSize = 0;  
            int resultMode = 0;  
          
            /* 
             * 根据模式判断 
             */  
            switch (specMode) {  
            case MeasureSpec.EXACTLY: // 父容器尺寸大小是一个确定的值  
                /* 
                 * 根据子元素的布局参数判断 
                 */  
                if (childDimension >= 0) { //如果childDimension是一个具体的值  
                    // 那么就将该值作为结果  
                    resultSize = childDimension;  
          
                    // 而这个值也是被确定的  
                    resultMode = MeasureSpec.EXACTLY;  
                } else if (childDimension == LayoutParams.MATCH_PARENT) { //如果子元素的布局参数为MATCH_PARENT  
                    // 那么就将父容器的大小作为结果  
                    resultSize = size;  
          
                    // 因为父容器的大小是被确定的所以子元素大小也是可以被确定的  
                    resultMode = MeasureSpec.EXACTLY;  
                } else if (childDimension == LayoutParams.WRAP_CONTENT) { //如果子元素的布局参数为WRAP_CONTENT  
                    // 那么就将父容器的大小作为结果  
                    resultSize = size;  
          
                    // 但是子元素的大小包裹了其内容后不能超过父容器  
                    resultMode = MeasureSpec.AT_MOST;  
                }  
                break;  
          
            case MeasureSpec.AT_MOST: // 父容器尺寸大小拥有一个限制值  
                /* 
                 * 根据子元素的布局参数判断 
                 */  
                if (childDimension >= 0) { //如果childDimension是一个具体的值  
                    // 那么就将该值作为结果  
                    resultSize = childDimension;  
          
                    // 而这个值也是被确定的  
                    resultMode = MeasureSpec.EXACTLY;  
                } else if (childDimension == LayoutParams.MATCH_PARENT) { //如果子元素的布局参数为MATCH_PARENT  
                    // 那么就将父容器的大小作为结果  
                    resultSize = size;  
          
                    // 因为父容器的大小是受到限制值的限制所以子元素的大小也应该受到父容器的限制  
                    resultMode = MeasureSpec.AT_MOST;  
                } else if (childDimension == LayoutParams.WRAP_CONTENT) { //如果子元素的布局参数为WRAP_CONTENT  
                    // 那么就将父容器的大小作为结果  
                    resultSize = size;  
          
                    // 但是子元素的大小包裹了其内容后不能超过父容器  
                    resultMode = MeasureSpec.AT_MOST;  
                }  
                break;  
          
            case MeasureSpec.UNSPECIFIED: // 父容器尺寸大小未受限制  
                /* 
                 * 根据子元素的布局参数判断 
                 */  
                if (childDimension >= 0) { //如果childDimension是一个具体的值  
                    // 那么就将该值作为结果  
                    resultSize = childDimension;  
          
                    // 而这个值也是被确定的  
                    resultMode = MeasureSpec.EXACTLY;  
                } else if (childDimension == LayoutParams.MATCH_PARENT) { //如果子元素的布局参数为MATCH_PARENT  
                    // 因为父容器的大小不受限制而对子元素来说也可以是任意大小所以不指定也不限制子元素的大小  
                    resultSize = 0;  
                    resultMode = MeasureSpec.UNSPECIFIED;  
                } else if (childDimension == LayoutParams.WRAP_CONTENT) { //如果子元素的布局参数为WRAP_CONTENT  
                    // 因为父容器的大小不受限制而对子元素来说也可以是任意大小所以不指定也不限制子元素的大小  
                    resultSize = 0;  
                    resultMode = MeasureSpec.UNSPECIFIED;  
                }  
                break;  
            }  
          
            // 返回封装后的测量规格  
            return MeasureSpec.makeMeasureSpec(resultSize, resultMode);  
        }  

至此我们可以看到一个View的大小由其父容器的测量规格MeasureSpec和View本身的布局参数LayoutParams共同决定，但是即便如此，最终封装的测量规格也是一个期望值，究竟有多大还是我们调用setMeasuredDimension方法设置的。        

__总结__      

整个测量流程:     
ViewGroup:  
由ViewRootImpl.performMeasure中调用我们根ViewG的measure方法开始  
而后根ViewGroup.measure中调用 onMeasure()方法(重写的方法)       
在onMeasure()方法中 可以measureChildren，measureChild，measureChildWithMargins等方法计算出子View的测量模式再调用子View.measure()方法由子View去计算自己的宽高   
同时计算出自己的宽高使用setMeasuredDimension(resultWidth, resultHeight);  设置    

子View:
子View的measure中也会去调用onMeasure去计算自己的宽高并也使用setMeasuredDimension(resultWidth, resultHeight);  设置    

__注意：计算宽高时:ViewGroup处理自己的内边距和子View的外边距__       
__子View需要计算自己的内边距__