package com.mmarciniak.diseasetest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.activity_test.view.*
import kotlinx.android.synthetic.main.disease_description_dialog.view.*
import java.lang.IllegalStateException

class CustomDialogFragment : DialogFragment() {

    private lateinit var diseaseName: String
    private lateinit var diseaseContent: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        diseaseName = arguments?.getString(diseaseNameKey) ?: throw IllegalStateException("No args provided")
        diseaseContent = arguments?.getString(diseaseContentKey) ?: throw IllegalStateException("No args provided")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View = inflater.inflate(R.layout.disease_description_dialog, container, false)
        val diseaseLabelText = "Disease: $diseaseName"
        rootView.description_label.text = diseaseLabelText
        rootView.description_content.text = diseaseContent

        rootView.cancel_button.setOnClickListener {
            dismiss()
        }

        return rootView
    }

    companion object {
        private const val diseaseNameKey = "diseaseName"
        private const val diseaseContentKey = "diseaseContentKey"
        @JvmStatic
        fun newInstance(diseaseName: String, diseaseContent: String): CustomDialogFragment =
            CustomDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(diseaseNameKey, diseaseName)
                    putString(diseaseContentKey, diseaseContent)
                }
            }
    }
}