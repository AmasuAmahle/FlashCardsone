package com.example.flashcards

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var welcomeLayout: View
    private lateinit var cardLayout: View
    private lateinit var scoreLayout: View

    private lateinit var tvCardText: TextView
    private lateinit var btnMyth: Button
    private lateinit var btnHack: Button
    private lateinit var btnNext: Button
    private lateinit var tvProgress: TextView
    private lateinit var rvScore: RecyclerView
    private lateinit var tvScoreSummary: TextView
    private lateinit var btnRestart: Button

    //Flashcard questions: Pair(Question, isMyth)
    private val flashcards = listOf(
        Pair("Public Wi-Fi is always safe to use for banking.", true),
        Pair("Two-factor authentication adds an extra layer of security.", false),
        Pair("Using the same password for all accounts is a good practice.", true),
        Pair("Hackers only target large corporations.", true),
        Pair("Updating software helps fix security vulnerabilities.", false),
        Pair("Placing your alarm clock across the room assists with getting out of bed in the morning", false),
        Pair("Placing a wooden spoon(uphinini) across the pot when boiling starchy foods prevent the water from spilling over", true),
        Pair("Putting toothpaste on mosquito bites stops them from itching", false),
        Pair("To pull a tightly plugged cork out of wine/champagne bottle jam a nail into it and pull on it to pull the cork out along with it", true),
        Pair("Turning your phone to airplane mode will help it charge faster", false)
    )

    private var index = 0
    private val userAnswers = MutableList(flashcards.size) { null as Boolean? }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        welcomeLayout = findViewById(R.id.welcomeLayout)
        cardLayout = findViewById(R.id.cardLayout)
        scoreLayout = findViewById(R.id.scoreLayout)

        tvCardText = findViewById(R.id.tvCardText)
        btnMyth = findViewById(R.id.btnMyth)
        btnHack = findViewById(R.id.btnHack)
        btnNext = findViewById(R.id.btnNext)
        tvProgress = findViewById(R.id.tvProgress)

        rvScore = findViewById(R.id.rvScore)
        tvScoreSummary = findViewById(R.id.tvScoreSummary)
        btnRestart = findViewById(R.id.btnRestart)

        findViewById<Button>(R.id.btnStart).setOnClickListener {
            startQuiz()
        }

        btnMyth.setOnClickListener { onAnswer(true) }
        btnHack.setOnClickListener { onAnswer(false) }
        btnNext.setOnClickListener { onNext() }

        btnRestart.setOnClickListener {
            restart()
        }

        showWelcome()
    }

    private fun showWelcome() {
        welcomeLayout.visibility = View.VISIBLE
        cardLayout.visibility = View.GONE
        scoreLayout.visibility = View.GONE
    }

    private fun startQuiz() {
        index = 0
        userAnswers.fill(null)
        showCard()
    }

    private fun showCard() {
        welcomeLayout.visibility = View.GONE
        cardLayout.visibility = View.VISIBLE
        scoreLayout.visibility = View.GONE

        val (text, _) = flashcards[index]
        tvCardText.text = text
        tvProgress.text = getString(R.string.progress_text, index + 1, flashcards.size)
        clearAnswerButtons()
        btnNext.isEnabled = false
    }

    private fun clearAnswerButtons() {
        btnMyth.setBackgroundColor(Color.LTGRAY)
        btnHack.setBackgroundColor(Color.LTGRAY)
    }

    private fun onAnswer(isMyth: Boolean) {
        userAnswers[index] = isMyth

        if (isMyth) {
            btnMyth.setBackgroundColor("#2196F3".toColorInt()) // Blue
            btnHack.setBackgroundColor(Color.LTGRAY)
        } else {
            btnHack.setBackgroundColor("#4CAF50".toColorInt()) // Green
            btnMyth.setBackgroundColor(Color.LTGRAY)
        }

        btnNext.isEnabled = true
    }

    private fun onNext() {
        if (userAnswers[index] == null) return

        if (index < flashcards.size - 1) {
            index++
            showCard()
        } else {
            showScore()
        }
    }

    private fun showScore() {
        welcomeLayout.visibility = View.GONE
        cardLayout.visibility = View.GONE
        scoreLayout.visibility = View.VISIBLE

        val results = flashcards.mapIndexed { i, pair ->
            val correctIsMyth = pair.second
            val userAnswer = userAnswers[i]
            val userCorrect = userAnswer == correctIsMyth
            Pair(i + 1, userCorrect)
        }

        val correctCount = results.count { it.second }
        val scoreText = getString(R.string.score_summary, correctCount, flashcards.size)
        val message = if (correctCount < 5) {
            getString(R.string.score_low)
        } else {
            getString(R.string.score_high)
        }
        
        tvScoreSummary.text = getString(R.string.score_summary_with_msg, scoreText, message)

        rvScore.layoutManager = LinearLayoutManager(this)
        rvScore.adapter = ScoreAdapter(flashcards.map { it.first }, results.map { it.second })
    }

    private fun restart() {
        showWelcome()
    }
}
