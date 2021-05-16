package ru.marslab.games.ui

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.marslab.games.R

const val INIT_POSITION = 0.5f
const val GAME_STEP = 0.01f

class MainActivity : AppCompatActivity() {
    private var brakeLine: ConstraintSet = ConstraintSet()
    private lateinit var gameField: ConstraintLayout
    private val position: MutableLiveData<Float> = MutableLiveData(INIT_POSITION)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initGame()
    }

    private fun initGame() {
        position.observe(this) {
            brakeLine.setGuidelinePercent(R.id.brakeLine, position.value!!)
            brakeLine.applyTo(gameField)
        }
        brakeLine.clone(applicationContext, R.layout.activity_main)
        gameField = findViewById(R.id.gameField)
        GlobalScope.launch {
            findViewById<FrameLayout>(R.id.upField).setOnClickListener {
                changePosition(true)
            }
        }
        GlobalScope.launch {
            findViewById<FrameLayout>(R.id.downField).setOnClickListener {
                changePosition(false)
            }
        }
    }

    private fun changePosition(direction: Boolean) {
        val oldPosition = position.value!!
        if (direction) {
            position.value = oldPosition.plus(GAME_STEP)
        } else {
            position.value = oldPosition.minus(GAME_STEP)
        }
    }

}