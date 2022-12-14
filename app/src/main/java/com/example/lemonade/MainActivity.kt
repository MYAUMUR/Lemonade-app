package com.example.lemonade

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    companion object {
        private const val LEMONADE_STATE = "LEMONADE_STATE"
        private const val LEMON_SIZE = "LEMON_SIZE"
        private const val SQUEEZE_COUNT = "SQUEEZE_COUNT"
        private const val SELECT = "select"
        private const val SQUEEZE = "squeeze"
        private const val DRINK = "drink"
        private const val RESTART = "restart"
    }

    private var lemonadeState = "select"
    private var lemonSize = -1
    private var squeezeCount = -1

    private var lemonTree = LemonTree()
    private var lemonImage: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            lemonadeState = savedInstanceState.getString(LEMONADE_STATE, "select")
            lemonSize = savedInstanceState.getInt(LEMON_SIZE, -1)
            squeezeCount = savedInstanceState.getInt(SQUEEZE_COUNT, -1)
        }

        lemonImage = findViewById(R.id.image_lemon_state)
        setViewElements()
        lemonImage!!.setOnClickListener { clickLemonImage() }
        lemonImage!!.setOnLongClickListener { showSnackbar() }
    }

    /**
     * This method saves the state of the app if it is put in the background.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(LEMONADE_STATE, lemonadeState)
        outState.putInt(LEMON_SIZE, lemonSize)
        outState.putInt(SQUEEZE_COUNT, squeezeCount)
        super.onSaveInstanceState(outState)
    }

    /**
     * Clicking will elicit a different response depending on the state.
     * This method determines the state and proceeds with the correct action.
     */
    private fun clickLemonImage() {
        when (lemonadeState) {
            SELECT -> {
                lemonadeState = SQUEEZE
                lemonSize = lemonTree.pick()
                squeezeCount = 0
            }
            SQUEEZE -> {
                squeezeCount++
                lemonSize--
                if (lemonSize == 0) lemonadeState = DRINK
            }
            DRINK -> {
                lemonadeState = RESTART
                lemonSize = -1
            }
            RESTART -> {
                lemonadeState = SELECT
            }
        }
        setViewElements()
    }

    /**
     * Set up the view elements according to the state.
     */
    private fun setViewElements() {
        when (lemonadeState) {
            SELECT -> setResources(R.drawable.lemon_tree, R.string.lemon_select)
            SQUEEZE -> setResources(R.drawable.lemon_squeeze, R.string.lemon_squeeze)
            DRINK -> setResources(R.drawable.lemon_drink, R.string.lemon_drink)
            RESTART -> setResources(R.drawable.lemon_restart, R.string.lemon_empty_glass)
        }
    }

    private fun setResources(image: Int, string: Int) {
        val textAction: TextView = findViewById(R.id.text_action)
        lemonImage?.setImageResource(image)
        textAction.text = getString(string)
    }

    /**
     * Long clicking the lemon image will show how many times the lemon has been squeezed.
     */
    private fun showSnackbar(): Boolean {
        if (lemonadeState != SQUEEZE) {
            return false
        }
        val squeezeText = getString(R.string.squeeze_count, squeezeCount)
        Snackbar.make(
            findViewById(R.id.constraint_Layout), squeezeText, Snackbar.LENGTH_SHORT
        ).show()
        return true
    }
}

/**
 * A Lemon tree class with a method to "pick" a lemon. The "size" of the lemon is randomized
 * and determines how many times a lemon needs to be squeezed before you get lemonade.
 */
class LemonTree {
    fun pick(): Int = (2..4).random()
}
