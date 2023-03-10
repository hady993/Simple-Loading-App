package com.udacity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.animation.addListener
import androidx.core.content.ContextCompat
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

/**
 * Step 2: Create a custom loading button by extending View class and assigning custom attributes to it.
     * 2.2: Create a LoadingButton class that extends the View class!
 */
class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    // 2.4.2.0: Define the loadingWidthSize variable!
    private var loadingWidthSize = 0F

    // 2.4.3.0: Define the textWidth, textSize variables!
    private var textWidth = 0F
    private var textSize = resources.getDimension(R.dimen.default_text_size)
    private var loadingCircle = 0

    // 2.4.4.0: Define the buttonText variable!
    private lateinit var buttonText: String

    // 2.6.2.3: Define the button, loadingBackground, loadingCircle's colors!
    private var buttonColor = 0
    private var loadingBackgroundColor = 0
    private var loadingCircleColor = 0

    private var valueAnimator = ValueAnimator()

    // 2.5: Create a button state object to update the button states according to download states!
    // Step 3: Animate properties of the custom button once it’s clicked!
    // We are using the [Delegates.observable] concept. These links are helpful:
    // https://medium.com/androiddevelopers/built-in-delegates-4811947e781f
    // https://medium.com/backyard-programmers/kotlin-standard-delegates-lazy-observable-and-vetoable-761a82b74e57
    var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when(new) {

            ButtonState.Clicked -> {
                invalidate()
            } // end ButtonState.Clicked

            ButtonState.Loading -> {

                // A: Update the button's text!
                buttonText = resources.getString(R.string.button_loading)

                // 3.1: Establish the button's animation!
                // Useful link: https://stackoverflow.com/questions/33870408/android-how-to-use-valueanimator
                valueAnimator = ValueAnimator.ofInt(0, 360).setDuration(1000).apply {

                    // 3.1.1: Update the value with animation and then redraw according to it!
                    addUpdateListener {
                        loadingCircle = it.animatedValue as Int
                        loadingWidthSize = (loadingCircle/360F) * widthSize
                        invalidate()
                    } // end UpdateListener

                    // 3.1.2: When the animation end reload it!
                    addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator?) {
                            loadingCircle = 0
                            if (buttonState == ButtonState.Loading) {
                                buttonState = ButtonState.Loading
                            } // end if()
                        } // end onAnimationEnd()
                    }) // end addListener()

                    interpolator = LinearInterpolator()
                    repeatCount = ValueAnimator.INFINITE
                    repeatMode = ValueAnimator.RESTART

                    // 3.1.3: Start the animation!
                    start()
                } // end apply

            } // end ButtonState.Loading

            ButtonState.Completed -> {
                // 3.2: Cancel the animation and reset the values!
                valueAnimator.cancel()
                loadingCircle = 0
                loadingWidthSize = 0F

                // A: Update the button's text!
                buttonText = resources.getString(R.string.button_name)

                // B: Redraw the view!
                invalidate()
            } // end ButtonState.Completed

        } // end when()
    } // end Delegates.observable<>()

    // 2.3: Create a paint object for the drawing styling (reuse the DialView code in the lesson 2)!
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = resources.getDimension(R.dimen.default_text_size)
        typeface = Typeface.create( "", Typeface.BOLD)
    } // end Paint()

    // 2.6: Initialize our custom view.
    init {
        // 2.6.1: Make it clickable and initialize its default text!
        isClickable = true
        buttonText = resources.getString(R.string.button_name)

        // 2.6.2: Establish our view's style.
        // Note: Reuse the DialView code in the lesson 2!
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            buttonColor = getColor(R.styleable.LoadingButton_buttonColor, 0)
            loadingBackgroundColor = getColor(R.styleable.LoadingButton_loadingBackgroundColor, 0)
            loadingCircleColor = getColor(R.styleable.LoadingButton_loadingCircleColor, 0)
        } // end context.withStyledAttributes()
    } // end init


    /**
     * 2.4: Perform the onDraw() functionality!
         * 2.4.1: Draw the Static Background!
         * 2.4.2: Draw the Loading Background!
         * 2.4.3: Draw the Loading Circle!
         * 2.4.4: Draw the Button Text!
     */
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawStaticBackground(canvas)
        drawLoadingBackground(canvas)
        drawLoadingCircle(canvas)
        drawButtonText(canvas)
    } // end onDraw()

    /**
     * 2.4.1: Draw the Static Background!
     */
    private fun drawStaticBackground(canvas: Canvas?) {
        // 2.4.1.1: Set button background color.
        paint.color = buttonColor

        // 2.4.1.2: Draw the button.
        canvas?.drawRoundRect(0F, 0F, widthSize.toFloat(), heightSize.toFloat(), 40F, 40F, paint)
    } // end drawStaticBackground()

    /**
     * 2.4.2: Draw the Loading Background!
     */
    private fun drawLoadingBackground(canvas: Canvas?) {
        // 2.4.2.1: Set loading button background color.
        paint.color = loadingBackgroundColor

        // 2.4.2.2: Draw the loading button.
        canvas?.drawRoundRect(0F, 0F, loadingWidthSize, heightSize.toFloat(), 40F, 40F, paint)
    } // end drawLoadingBackground()

    /**
     * 2.4.3: Draw the Loading Circle!
     */
    private fun drawLoadingCircle(canvas: Canvas?) {
        // 2.4.3.1: Save the drawing progress!
        canvas?.save()

        // 2.4.3.2: Establish the circle's position!
        canvas?.translate(widthSize/2 + textWidth/2 + textSize, heightSize/2 - textSize/2)

        // 2.4.3.3: Set loading circle background color.
        paint.color = loadingCircleColor

        // 2.4.3.4: Draw the loading circle.
        canvas?.drawArc(0F, 0F, textSize, textSize,
            0F, loadingCircle.toFloat(), true, paint)

        // 2.4.3.4: Use restore to reset the state to its original.
        canvas?.restore()
    } // end drawLoadingCircle()

    /**
     * 2.4.4: Draw the Button Text!
     */
    private fun drawButtonText(canvas: Canvas?) {
        // 2.4.4.1: Set button text color.
        paint.color = ContextCompat.getColor(context, R.color.white)

        // 2.4.4.2: Set the button text to be measured.
        textWidth = paint.measureText(buttonText)

        // 2.4.4.3: Draw the button text.
        // Note: This link is helpful: https://stackoverflow.com/questions/27631736/meaning-of-top-ascent-baseline-descent-bottom-and-leading-in-androids-font
        canvas?.drawText(buttonText,
            widthSize/2F, heightSize/2 - (paint.ascent() + paint.descent())/2, paint)
    } // end drawButtonText()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    } // end onMeasure()

} // end class