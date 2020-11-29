package com.mmarciniak.diseasetest.fragments

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.mmarciniak.diseasetest.R
import kotlinx.android.synthetic.main.quiz_result_dialog.view.*


class QuizResultDialogFragment : DialogFragment() {

    private var scoreValue: Int = 0
    private var closeActivity: Boolean = false
    private lateinit var mListener: OnQuizCompleteListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scoreValue = arguments?.getInt(scoreKey) ?: throw IllegalStateException("No args provided")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View = inflater.inflate(R.layout.quiz_result_dialog, container, false)
        val quizStatusText = "Score: $scoreValue/10"
        rootView.quiz_result_label.text = quizStatusText

        rootView.no_button.setOnClickListener {
            closeActivity = true
            dismiss()
        }

        rootView.yes_button.setOnClickListener {
            closeActivity = false
            mListener.onComplete(true)
            dismiss()
        }

        return rootView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            mListener = activity as OnQuizCompleteListener
        }
        catch (e: ClassCastException)
        {
            throw ClassCastException(activity.toString() + " must implement OnCompleteListener")
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (closeActivity) {
            val activity: Activity? = activity
            if (activity is DialogInterface.OnDismissListener) {
                (activity as DialogInterface.OnDismissListener).onDismiss(dialog)
            }
        }
    }


    companion object {
        private const val scoreKey = "score"

        @JvmStatic
        fun newInstance(score: Int): QuizResultDialogFragment =
            QuizResultDialogFragment().apply {
                arguments = Bundle().apply {
                    putInt(scoreKey, score)
                }
            }
    }
}