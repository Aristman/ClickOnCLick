package ru.marslab.games.ui

import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
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
        when (game()) {
            1 -> Toast.makeText(applicationContext, "верх!!!", Toast.LENGTH_SHORT).show()
            2 -> Toast.makeText(applicationContext, "Низ!!!", Toast.LENGTH_SHORT).show()
        }
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
//        position.observe(this) {
//            brakeLine.setGuidelinePercent(R.id.brakeLine, position.value!!)
//            brakeLine.applyTo(gameField)
//        }
//        brakeLine.clone(applicationContext, R.layout.activity_main)

    }

    private fun game(): Int {
        GlobalScope.launch {
            clickCallbackFlow.collect {
                position += it
            }
        }
        while (true) {
            brakeLine.apply {
                setGuidelinePercent(R.id.brakeLine, position)
                applyTo(gameField)
            }
            gameField.refreshDrawableState()
            if (position <= 0) return 1
            if (position > 1) return 2
        }
    }

}