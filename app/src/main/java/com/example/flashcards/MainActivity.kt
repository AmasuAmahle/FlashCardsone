package com.example.flashcards

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompActivity
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

    //Flashcard questions
    private val flashcards = listOf(
        Pair("")
    )

    private var index = 0
    private var lastAnswerIsMyth: Boolean? = null
    private val userAnswers = MutableList(flashcards.size) {null as Boolean?}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.id.activity_main)

        welcomeLayout = findViewById(R.id.welcomeLayout)
        cardLayout = findViewById(R.id.cardLayout)
        scoreLayout=findViewById(R.id.scoreLayout)

        tvCardText = findViewById(R.id.tvCardText)
        btnMyth = findViewById(R.id.btnMyth)
        btnHack = findViewById(R.id.btnHack)
        btnNext = findViewById(R.id.btnNext)
        tvProgress = findViewById(R.id.tvProgress)

        rvScore = findViewById(R.id.rvScore)
        tvScoreSummary = findViewById(R.id.tvScoreSummary)
        btnRestart = findViewById(R.id.btnRestart)

        btnMyth.setOnClickListener { onAnswer(true) }
        btnHack.setOnClickListener { onAnswer(false)}
        btnNext.setOnClickListener { onNext() }

        findViewById<Button>(R.id.btnRestart).setOnClickListener {
            startQuiz()
        }

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
    private fun startQuiz(){
        index = 0
        for (i in userAnswers.indices) userAnswers[i] = null
        showCard()
    }

    private fun showCard(){
        welcomeLayout.visibility = View.GONE
        cardLayout.visibility = View.VISIBLE
        scoreLayout.visibility = View.GONE

        val (text, _) = flashcards[index]
        tvCardText.text = text
        tvProgress.text = "Card ${index + 1} / ${flashcards.size}"
        clearAnswerButtons()
        btnNext.isEnabled = false
    }

    private fun clearAnswerButtons(){
        btnMyth.setBackgroundColor(getColor(android.R.color.darker_gray))
        btnHack.setBackgroundColor(getColor(android.R.color.darker_gray))
        lastAnswerIsMyth = null
    }

    private fun onAnswer(isMyth: Boolean){
        //record of answer selections
        userAnswers[index] = isMyth
        lastAnswerIsMyth = isMyth

        //visual feedback
        if (isMyth){
            btnMyth.setBackgroundColor(Color.parseColor("#2196F3"))//blue
            btnHack.setBackgroundColor(getColor(android.R.color.darker_gray))
        } else {
            btnHack.setBackgroundColor(Color.parseColor("#4CAF50")) //green
            btnMyth.setBackgroundColor(getColor(android.R.color.darker_gray))
        }

        btnNext.isEnabled = true
    }

    private fun onNext() {
        //validation of user input
        if (userAnswers[index]== null) return

        //move on to the next or finish if enterd
        if (index < flashcards.size - 1) {
            index++
            showCard()
        } else {
            showScore()
        }
    }

    private fun showScore(){
        welcomeLayout.visibility = View.GONE
        cardLayout.visibility = View.GONE
        scoreLayout.visibility = View.VISIBLE

        val results = flashcards.mapIndexed { i, pair ->
            val correctIsMyth = pair.second

            //"Myth"= false
            //"Hack"= true
            val userAnswer = userAnswers[i]
            val userCorrect = when (userAnswer) {
                null -> false
                true -> !pair.second
                false -> pair.second
            }
            Pair(i + 1, userCorrect)
        }

        val correctCount = results.count { it.second }
        tvScoreSummary.text = "Your score is $correctCount / ${flashcards.size}"

        rvScore.layoutManager = LinearLayoutManager(this)
        rvScore.adapter = ScoreAdapter(flashcards.map { it.first }, results.map { it.second })
    }

    private fun restart() {
        showWelcome()
    }
}
