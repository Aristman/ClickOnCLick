package ru.marslab.games.ui

import android.graphics.Canvas
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import ru.marslab.games.R

const val INIT_POSITION = 0.5f
const val GAME_STEP = 0.005f

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {
    private lateinit var clickCallbackFlow: Flow<Float>
    private var brakeLine: ConstraintSet = ConstraintSet()
    private val upFrame by lazy { findViewById<FrameLayout>(R.id.upField) }
    private val downFrame by lazy { findViewById<FrameLayout>(R.id.downField) }
    private var position: Float = INIT_POSITION
    private val gameField by lazy { findViewById<ConstraintLayout>(R.id.gameField) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initGame()
        game()
    }

    private fun initGame() {
        clickCallbackFlow = callbackFlow {
            upFrame.setOnClickListener {
                offer(GAME_STEP)
            }
            downFrame.setOnClickListener {
                offer(-GAME_STEP)
            }
            awaitClose {
                upFrame.setOnClickListener(null)
                downFrame.setOnClickListener(null)
            }
        }
        GlobalScope.launch {
            clickCallbackFlow.collect {
                position += it
            }
        }
    }

    private fun game() {
        brakeLine.clone(applicationContext, R.layout.activity_main)
        GlobalScope.launch {
            while (true) {
                withContext(Dispatchers.Main) {
                    brakeLine.setGuidelinePercent(R.id.brakeLine, position)
                    brakeLine.applyTo(gameField)
                }
                println(position)
                delay(50)
            }
        }
    }

}