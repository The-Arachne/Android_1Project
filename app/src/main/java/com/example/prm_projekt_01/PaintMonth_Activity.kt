package com.example.prm_projekt_01

import android.graphics.*
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.prm_projekt_01.databinding.ActivityPaintMonthBinding
import java.time.LocalDate

class PaintMonth_Activity : AppCompatActivity() {

    val binding by lazy { ActivityPaintMonthBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val array = arrayOf(
                "30dni",
                "60dni",
                "90dni",
                "180dni",
                "360dni"
        )

        //add previously created list of choices to spinner
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, array)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.apply {
            adapter = aa
            gravity = Gravity.CENTER
            //adding action listener to this spinner
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                ) {
                    binding.imageView.setImageBitmap(drawPath(binding.imageView, array[position]))
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
            //set default selected
            setSelection(0)
        }
    }

    //magic method to draw income/spendings
    @RequiresApi(Build.VERSION_CODES.O)
    private fun drawPath(imageView: ImageView, lastDays: String): Bitmap? {

        //Preparing bitmap & canvas
        val bitmap = Bitmap.createBitmap(
                imageView.width,
                imageView.height,
                Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap).apply { drawColor(Color.LTGRAY) }

        //Preparing colors
        val paintRed = Paint().apply {
            color = Color.RED
            strokeWidth = 10F
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
        }
        val paintGreen = Paint(paintRed).apply { color = Color.GREEN }
        val paintBounds = Paint(paintRed).apply {
            color = Color.BLACK
            strokeWidth = 2F
        }
        val paintText = Paint().apply {
            isAntiAlias = true
            textSize = 35f
            color = Color.BLACK
            typeface = Typeface.DEFAULT
            textAlign = Paint.Align.CENTER
        }

        //today minus selected from spinner
        val date = LocalDate.now().minusDays(lastDays.replace("dni", "").toLong())

        //get list of balance's from all values based on previously computed date
        val tmpDate = Shared.getPart_byDate(date)
        //is null or empty?
        if (tmpDate == null || tmpDate.size == 0)
            return bitmap
        val balances = Shared.getBalance(tmpDate.reversed())

        //get min/max value based on previously computed date
        var maxVal: Int = ((balances.maxOf { a -> a.value }.toInt() + 99) / 100) * 100
        var minVal: Int = ((balances.minOf { a -> a.value }.toInt() - 99) / 100) * 100
        if (minVal > 0) minVal = 0
        if (maxVal < 0) maxVal = 0

        //creating bonds
        val margin = 50
        val maxHeigh = imageView.height - 4 * margin
        val maxWidth = imageView.width - 5 * margin

        //creating converters(pixel to balance and time(day's) to pixels)
        val difference = (maxVal - minVal)
        val pixToMoney = difference / (maxHeigh.toDouble())
        val dayToPix = maxWidth / balances.size.toDouble()

        //drawing currency balance names (Y data)
        for (i in 0..10) {
            canvas.save()
            canvas.drawText((maxVal - (i * (difference / 10))).toString() + " zł", 2 * margin.toFloat(), (maxHeigh / 11f) * i + 2 * margin, paintText)
            canvas.restore()
            canvas.save()
        }

        //where should i put horizontal line?
        var yShouldBe = 0
        if (minVal * maxVal < 0)
            for (i in 0..maxHeigh)
                if ((minVal + i * pixToMoney) < 0 && (minVal + (i + 1) * pixToMoney) >= 0) {
                    yShouldBe = i
                    break
                }
        if (yShouldBe == 0) {
            if (minVal <= 0)
                yShouldBe = maxHeigh + margin
            if (maxVal >= 0)
                yShouldBe = margin
        }
        val yActualVal = (maxHeigh + 2 * margin - yShouldBe)

        //Creating graph bounds
        val graphBoundsPath = Path().apply {
            val topYBound = Point(4 * margin, margin)
            moveTo(topYBound.x.toFloat(), topYBound.y.toFloat())

            val bottomYBound = Point(4 * margin, maxHeigh + margin)
            lineTo(bottomYBound.x.toFloat(), bottomYBound.y.toFloat())

            val leftXBound = Point(4 * margin, yActualVal)
            moveTo(leftXBound.x.toFloat(), leftXBound.y.toFloat())

            val rightXBound = Point(canvas.width - margin, yActualVal)
            lineTo(rightXBound.x.toFloat(), rightXBound.y.toFloat())

            close()
        }

        //Drawing graph bounds
        canvas.save()
        canvas.drawPath(graphBoundsPath, paintBounds)
        canvas.restore()
        canvas.save()

        //drawing dates and snakes(balance lines)((X data))
        var lastXDate = 3 * margin
        var lastXDateSaved = 0
        var prevPoint = Point()
        balances.forEach { indx, balance ->
            //drawing bottom text id est date in graph
            if (lastXDate == 3 * margin || (lastXDate + dayToPix.toInt() >= lastXDateSaved + 100 && lastXDate < (canvas.width - 4 * margin))) {
                canvas.save()
                lastXDateSaved = lastXDate
                canvas.rotate(-45f, lastXDate.toFloat(), (maxHeigh + margin - yShouldBe).toFloat())
                canvas.drawText(indx.toString(), lastXDate.toFloat(), (maxHeigh + 2 * margin - yShouldBe + 130).toFloat(), paintText)
                canvas.restore()
                canvas.save()
            }
            lastXDate += dayToPix.toInt()
            //drawing green or red snakes
            if (prevPoint.x == 0) {
                prevPoint.apply {
                    x = lastXDate + margin - dayToPix.toInt()
                    y = (yActualVal + margin - ((balance * maxHeigh) / difference)).toInt()
                }
                if (balances.size == 1) {
                    canvas.save()
                    canvas.drawPath(Path().apply { addCircle(prevPoint.x.toFloat() + 30, prevPoint.y.toFloat(), 3f, Path.Direction.CCW) }, if (balance >= 0) paintGreen else paintRed)
                    canvas.restore()
                    canvas.save()
                }
            } else {
                val path = Path()
                path.moveTo(prevPoint.x.toFloat(), prevPoint.y.toFloat())
                prevPoint.x = (lastXDate.toFloat() + margin).toInt()
                prevPoint.y = (yActualVal + margin - ((balance * maxHeigh) / difference).toFloat()).toInt()
                path.lineTo(prevPoint.x.toFloat(), prevPoint.y.toFloat())
                canvas.save()
                canvas.drawPath(path, if (balance >= 0) paintGreen else paintRed)
                canvas.restore()
                canvas.save()
            }
        }
        return bitmap
    }
}
