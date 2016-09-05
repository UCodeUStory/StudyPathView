package com.example.qiyue.study;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by qiyue on 2016/8/25 0025.
 *
 * 让我们一起学习PathMeasure，
 * 然而学完可能并没有卵用，但是你不知道，那你就out了
 *
 * thank you http://blog.csdn.net/u013831257/article/details/51565591
 */
public class CustomView1 extends View{

    private int mViewWidth;

    private int mViewHeight;

    private Paint mDeafultPaint;

    private float currentValue = 0;     // 用于纪录当前的位置,取值范围[0,1]映射Path的整个长度

    private float[] pos;                // 当前点的实际位置
    private float[] tan;                // 当前点的tangent值,用于计算图片所需旋转的角度
    private Bitmap mBitmap;             // 箭头图片
    private Matrix mMatrix;

    public CustomView1(Context context) {
        super(context);
        init(context);
    }


    public CustomView1(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomView1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mDeafultPaint = new Paint();
        mDeafultPaint.setColor(Color.BLACK);
        mDeafultPaint.setStrokeWidth(10);
        mDeafultPaint.setStyle(Paint.Style.STROKE);


        pos = new float[2];
        tan = new float[2];
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;       // 缩放图片
        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.arrow, options);
        mMatrix = new Matrix();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
      //  learnDrawTwo(canvas);
      //  learnDrawThree(canvas);
     //   learnDrawFour(canvas);
       // learnDrawFive(canvas);
        learnDrawSix(canvas);
    }

    /**
     * 1学习Pathmeasure构造方法
     * @param canvas
     */
    private void learnDrawOne(Canvas canvas){
        /**平移画布到中心点，默认是0,0开始，也就是view的左上角**/
        canvas.translate(mViewWidth/2,mViewHeight/2);
        Path path = new Path();

        path.lineTo(0,200);
        path.lineTo(200,200);
        path.lineTo(200,0);
        /**
         * 1测量真实长度
         */
        PathMeasure measure1 = new PathMeasure(path,false);
        /**
         * 2测量闭合的长度，不准确
         */
        PathMeasure measure2 = new PathMeasure(path,true);

        Log.e("TAG", "forceClosed=false---->"+measure1.getLength());
        Log.e("TAG", "forceClosed=true----->"+measure2.getLength());

        canvas.drawPath(path,mDeafultPaint);
    }

    /**
     * 2学习getSegment
     * @param canvas
     */
    private void learnDrawTwo(Canvas canvas){
        canvas.translate(mViewWidth / 2, mViewHeight / 2);          // 平移坐标系

        Path path = new Path();                                     // 创建Path并添加了一个矩形
        path.addRect(-200, -200, 200, 200, Path.Direction.CW);

        Path dst = new Path();                                      // 创建用于存储截取后内容的 Path

        PathMeasure measure = new PathMeasure(path, false);         // 将 Path 与 PathMeasure 关联

        // 截取一部分存入dst中，并使用 moveTo 保持截取得到的 Path 第一个点的位置不变
        /**
         * 第一个参数，作为截取开始点 是距离起点距离
         * 第二个参数，作为截取结束点 也是距离起点的距离
         * dst 截取后保存到这里
         * 第四个参数,true 保存截取点位置不变，  false截取点就变成dst 路径中的终点
         */
        measure.getSegment(200, 600, dst, true);
       // measure.getSegment(200, 600, dst, false);

        canvas.drawPath(dst, mDeafultPaint);                        // 绘制 dst
    }

    /**
     *3 dang   dst 中有内容会追加path而不是替换
     * @param canvas
     */
    private void learnDrawThree(Canvas canvas){
        canvas.translate(mViewWidth / 2, mViewHeight / 2);          // 平移坐标系

        Path path = new Path();                                     // 创建Path并添加了一个矩形
        path.addRect(-200, -200, 200, 200, Path.Direction.CW);

        Path dst = new Path();                                      // 创建用于存储截取后内容的 Path
        dst.lineTo(-300, -300);
        PathMeasure measure = new PathMeasure(path, false);         // 将 Path 与 PathMeasure 关联

        // 截取一部分存入dst中，并使用 moveTo 保持截取得到的 Path 第一个点的位置不变
        /**
         * 第一个参数，作为截取开始点 是距离起点距离
         * 第二个参数，作为截取结束点 也是距离起点的距离
         * dst 截取后保存到这里
         * 第四个参数,true 保存截取点位置不变， false截取点就变成dst 路径中的终点
         */

        measure.getSegment(200, 600, dst, true);
        // measure.getSegment(200, 600, dst, false);

        canvas.drawPath(dst, mDeafultPaint);

    }

    /**
     * 4.nextContour
     */
    private void learnDrawFour(Canvas canvas){
        canvas.translate(mViewWidth / 2, mViewHeight / 2);      // 平移坐标系

        Path path = new Path();

        path.addRect(-100, -100, 100, 100, Path.Direction.CW);  // 添加小矩形
        path.addRect(-200, -200, 200, 200, Path.Direction.CW);  // 添加大矩形

        canvas.drawPath(path,mDeafultPaint);                    // 绘制 Path

        PathMeasure measure = new PathMeasure(path, false);     // 将Path与PathMeasure关联

        float len1 = measure.getLength();                       // 获得第一条路径的长度

        measure.nextContour();                                  // 跳转到下一条路径

        float len2 = measure.getLength();                       // 获得第二条路径的长度

        Log.i("LEN","len1="+len1);                              // 输出两条路径的长度
        Log.i("LEN","len2="+len2);

    }

    /**
     * 1.通过 tan 得值计算出图片旋转的角度，tan 是 tangent 的缩写，即中学中常见的正切，
     * 其中tan0是邻边边长，tan1是对边边长，而Math中 atan2 方法是根据正切是数值计算出该角度的大小,
     * 得到的单位是弧度，所以上面又将弧度转为了角度。
     2.通过 Matrix 来设置图片对旋转角度和位移，这里使用的方法与前面讲解过对 canvas操作 有些类似，
     对于 Matrix 会在后面专一进行讲解，敬请期待。
     3.页面刷新，页面刷新此处是在 onDraw 里面调用了 invalidate 方法来保持界面不断刷新，
     但并不提倡这么做，正确对做法应该是使用 线程 或者 ValueAnimator 来控制界面的刷新，
     关于控制页面刷新这一部分会在后续的 动画部分 详细讲解，同样敬请期待
     * @param canvas
     */
    private void learnDrawFive(Canvas canvas){
        canvas.translate(mViewWidth / 2, mViewHeight / 2);      // 平移坐标系

        Path path = new Path();                                 // 创建 Path

        path.addCircle(0, 0, 200, Path.Direction.CW);           // 添加一个圆形

        PathMeasure measure = new PathMeasure(path, false);     // 创建 PathMeasure

        currentValue += 0.005;                                  // 计算当前的位置在总长度上的比例[0,1]
        if (currentValue >= 1) {
            currentValue = 0;
        }

        measure.getPosTan(measure.getLength() * currentValue, pos, tan);        // 获取当前位置的坐标以及趋势

        mMatrix.reset();                                                        // 重置Matrix
        float degrees = (float) (Math.atan2(tan[1], tan[0]) * 180.0 / Math.PI); // 计算图片旋转角度

        mMatrix.postRotate(degrees, mBitmap.getWidth() / 2, mBitmap.getHeight() / 2);   // 旋转图片
        mMatrix.postTranslate(pos[0] - mBitmap.getWidth() / 2, pos[1] - mBitmap.getHeight() / 2);   // 将图片绘制中心调整到与当前点重合

        canvas.drawPath(path, mDeafultPaint);                                   // 绘制 Path
        canvas.drawBitmap(mBitmap, mMatrix, mDeafultPaint);                     // 绘制箭头

        invalidate();
    }

    /**
     * getMatrix 可以省略3行代码
     * @param canvas
     */
    private void learnDrawSix(Canvas canvas){
        canvas.translate(mViewWidth/2,mViewHeight/2);
        Path path = new Path();                                 // 创建 Path

        path.addCircle(0, 0, 200, Path.Direction.CW);           // 添加一个圆形

        PathMeasure measure = new PathMeasure(path, false);     // 创建 PathMeasure

        currentValue += 0.005;                                  // 计算当前的位置在总长度上的比例[0,1]
        if (currentValue >= 1) {
            currentValue = 0;
        }

        /**
         *     获取当前位置的坐标以及趋势的矩阵
         *     * 1.对 matrix 的操作必须要在 getMatrix 之后进行，否则会被 getMatrix 重置而导致无效。
         * 2.矩阵对旋转角度默认为图片的左上角，我们此处需要使用 preTranslate 调整为图片中心。
         * 3.pre(矩阵前乘) 与 post(矩阵后乘) 的区别，此处请等待后续的文章或者自行搜索。
         *boolean getMatrix (float distance, Matrix matrix, int flags)
         参数	作用	备注
         返回值(boolean)	判断获取是否成功	true表示成功，数据会存入matrix中，false 失败，matrix内容不会改变
         distance	距离 Path 起点的长度	取值范围: 0 <= distance <= getLength
         matrix	根据 falgs 封装好的matrix	会根据 flags 的设置而存入不同的内容
         flags	规定哪些内容会存入到matrix中	可选择
         POSITION_MATRIX_FLAG(位置)
         ANGENT_MATRIX_FLAG(正切)
          */
        measure.getMatrix(measure.getLength() * currentValue, mMatrix,
                PathMeasure.TANGENT_MATRIX_FLAG | PathMeasure.POSITION_MATRIX_FLAG);

        mMatrix.preTranslate(-mBitmap.getWidth() / 2, -mBitmap.getHeight() / 2);   // <-- 将图片绘制中心调整到与当前点重合(注意:此处是前乘pre)

        canvas.drawPath(path, mDeafultPaint);                                   // 绘制 Path
        canvas.drawBitmap(mBitmap, mMatrix, mDeafultPaint);                     // 绘制箭头

        invalidate();
    }



    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;
        Log.i("qiyue","w="+w);
        Log.i("qiyue","h="+h);
    }
}
