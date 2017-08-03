#### layout
ViewGroup,普通View控件都继承自View,他们的布局都是从View的layout方法开始。     
还是从根View开始，在ViewRootImpl.performTraversals()中根View在经过measure阶段以后，   
系统确定了根View的测量大小后，接下来就进入到layout的过程，    
就会对他和他下面的子View进行布局 

    private void performTraversals() {  
        // ………………  
         final boolean didLayout = layoutRequested && (!mStopped || mReportNextDraw);
              boolean triggerGlobalLayoutListener = didLayout
                      || mAttachInfo.mRecomputeGlobalAttributes;
              if (didLayout) {
                  performLayout(lp, mWidth, mHeight);
                 // .......
                }
      
        // ………………  
    } 
    
     private void performLayout(WindowManager.LayoutParams lp, int desiredWindowWidth,
                int desiredWindowHeight) {
            mLayoutRequested = false;
            mScrollMayChange = true;
            mInLayout = true;
    
            final View host = mView;
            if (DEBUG_ORIENTATION || DEBUG_LAYOUT) {
                Log.v(mTag, "Laying out " + host + " to (" +
                        host.getMeasuredWidth() + ", " + host.getMeasuredHeight() + ")");
            }
    
            Trace.traceBegin(Trace.TRACE_TAG_VIEW, "layout");
            try {
            //对根View进行布局
                host.layout(0, 0, host.getMeasuredWidth(), host.getMeasuredHeight());
    
               ///.......
            } finally {
                Trace.traceEnd(Trace.TRACE_TAG_VIEW);
            }
            //......
        }

在performLayout中调用了根View的layout方法进行布局

       /**
         * @param l Left position, relative to parent
         * @param t Top position, relative to parent
         * @param r Right position, relative to parent
         * @param b Bottom position, relative to parent
         */
        @SuppressWarnings({"unchecked"})
        public void layout(int l, int t, int r, int b) {
            if ((mPrivateFlags3 & PFLAG3_MEASURE_NEEDED_BEFORE_LAYOUT) != 0) {
                onMeasure(mOldWidthMeasureSpec, mOldHeightMeasureSpec);
                mPrivateFlags3 &= ~PFLAG3_MEASURE_NEEDED_BEFORE_LAYOUT;
            }
    
            int oldL = mLeft;
            int oldT = mTop;
            int oldB = mBottom;
            int oldR = mRight;
            //确定该View在其父View中的位置
            boolean changed = isLayoutModeOptical(mParent) ?
                    setOpticalFrame(l, t, r, b) : setFrame(l, t, r, b);
    
            if (changed || (mPrivateFlags & PFLAG_LAYOUT_REQUIRED) == PFLAG_LAYOUT_REQUIRED) {
                 //测量子布局
                onLayout(changed, l, t, r, b);
    
                if (shouldDrawRoundScrollbar()) {
                    if(mRoundScrollbarRenderer == null) {
                        mRoundScrollbarRenderer = new RoundScrollbarRenderer(this);
                    }
                } else {
                    mRoundScrollbarRenderer = null;
                }
    
                mPrivateFlags &= ~PFLAG_LAYOUT_REQUIRED;
    
                ListenerInfo li = mListenerInfo;
                if (li != null && li.mOnLayoutChangeListeners != null) {
                    ArrayList<OnLayoutChangeListener> listenersCopy =
                            (ArrayList<OnLayoutChangeListener>)li.mOnLayoutChangeListeners.clone();
                    int numListeners = listenersCopy.size();
                    for (int i = 0; i < numListeners; ++i) {
                        listenersCopy.get(i).onLayoutChange(this, l, t, r, b, oldL, oldT, oldR, oldB);
                    }
                }
            }
    
            mPrivateFlags &= ~PFLAG_FORCE_LAYOUT;
            mPrivateFlags3 |= PFLAG3_IS_LAID_OUT;
        }
        
layout方法中首先确认了View自己的位置，而后又去调用了onLayout()测量子View的位置     

    /**
     * 计算子View的位置，当自定义ViewGroup时应该覆盖此方法并在此方法中去调用子View的layout方法
     * 计算子 View的位置
     * @param changed This is a new size or position for this view
     * @param left Left position, relative to parent
     * @param top Top position, relative to parent
     * @param right Right position, relative to parent
     * @param bottom Bottom position, relative to parent
     */
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    }

由View中的onLayout方法可知，onLayout方法只有在定义ViewGroup时需要重写，普通View不需要重写,  
也就是普通控件的布局过程就是调用了View的默认过程调用View.layout()即可，不需要我们去覆盖onLayout        

ViewGroup中的onLayout,是一个抽象方法，所以他的子 View都会重写这个方法

    @Override
    protected abstract void onLayout(boolean changed,
            int l, int t, int r, int b);
            

 由以上可知整个布局过程：   
 
由ViewGroup.layout开始，确定了自己的位置之后，调用onLayout()去确定子View的位置  
在ViewGroup.onLayout()方法中，去调用子View.layout()方法去确定子View的位置，如此一层一层往下传递      
    
概况地讲：        
View的layout()方法用于View确定自己本身在其父View的位置   
ViewGroup的onLayout()方法用于确定子View的位置,同时也要记得处理自己的padding和子View的layoutMargins  
