package magicchief.main.brewersservant.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import magicchief.main.brewersservant.R

class SettingsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val aboutButton = requireView().findViewById<MaterialButton>(R.id.about_button)
        val disclaimerButton = requireView().findViewById<MaterialButton>(R.id.disclaimer_button)

        aboutButton.setOnClickListener {
            val aboutDialog = LayoutInflater.from(requireContext()).inflate(R.layout.about_dialog, null, false)
            val dialog = MaterialAlertDialogBuilder(requireContext())
                .setView(aboutDialog)
                .setTitle(R.string.about_title)
                .show()
        }
        disclaimerButton.setOnClickListener {
            val disclaimerDialog = LayoutInflater.from(requireContext()).inflate(R.layout.disclaimer_dialog, null, false)
            val dialog = MaterialAlertDialogBuilder(requireContext())
                .setView(disclaimerDialog)
                .setTitle(R.string.disclaimer_title)
                .show()
        }
    }
}