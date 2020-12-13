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
import kotlinx.android.synthetic.main.test2_result_dialog.view.*
import java.lang.IllegalStateException

class Test2ResultDialogFragment : DialogFragment() {

    private var diseaseName: String = ""
    private var match: Boolean = false

    private lateinit var mListener: OnQuizCompleteListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        diseaseName =
            arguments?.getString(diseaseNameKey) ?: throw IllegalStateException("No args provided")
        match = arguments?.getBoolean(matchKey) ?: throw IllegalStateException("No args provided")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View = inflater.inflate(R.layout.test2_result_dialog, container, false)
        dialog?.setCancelable(false);

        if (match) {
            rootView.test2_result_label.text = "You were right"
        } else {
            rootView.test2_result_label.text = "You were wrong"
        }
        val diseaseLabelText ="disease name was:\n $diseaseName"
        rootView.test2_tv.text = diseaseLabelText

        rootView.go_back_button.setOnClickListener {
            mListener.onComplete(false)
            dismiss()
        }

        rootView.show_drugs_btn.setOnClickListener {
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


    companion object {
        private const val diseaseNameKey = "diseaseName"
        private const val matchKey = "match"

        @JvmStatic
        fun newInstance(diseaseName: String, match: Boolean): Test2ResultDialogFragment =
            Test2ResultDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(diseaseNameKey, diseaseName)
                    putBoolean(matchKey, match)
                }
            }
    }
}