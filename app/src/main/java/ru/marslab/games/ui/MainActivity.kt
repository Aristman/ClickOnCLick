package ru.marslab.games.ui

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import ru.marslab.games.R

const val INIT_POSITION = 0.5f
const val GAME_STEP = 0.01f

class MainActivity : AppCompatActivity() {
    private var brakeLine: ConstraintSet = ConstraintSet()
    private lateinit var gameField: ConstraintLayout
    private var position: Float = INIT_POSITION

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initGame()
    }

    private fun initGame() {
        brakeLine.clone(applicationContext, R.layout.activity_main)
        gameField = findViewById(R.id.gameField)
        findViewById<FrameLayout>(R.id.upField).setOnClickListener {
            position += GAME_STEP
            brakeLine.setGuidelinePercent(R.id.brakeLine, position)
            brakeLine.applyTo(gameField)
        }
        findViewById<FrameLayout>(R.id.downField).setOnClickListener {
            position -= GAME_STEP
            brakeLine.setGuidelinePercent(R.id.brakeLine, position)
            brakeLine.applyTo(gameField)
        }
    }


}