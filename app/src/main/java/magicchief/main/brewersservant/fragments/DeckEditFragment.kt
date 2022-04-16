package magicchief.main.brewersservant.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import magicchief.main.brewersservant.DBHelper
import magicchief.main.brewersservant.R

class DeckEditFragment : Fragment() {

    var deckId = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            deckId = it.getInt("deckId")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_deck_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dbHelper = DBHelper(requireContext())

        val deck = dbHelper.getDeck(deckId)

        val deckNameTextView = requireView().findViewById<TextInputLayout>(R.id.edit_deck_name_text)
        val deckFaceCardTextView = requireView().findViewById<TextInputLayout>(R.id.edit_deck_face_card_text)
        val saveButton = requireView().findViewById<MaterialButton>(R.id.edit_deck_save_button)

        deckNameTextView.editText?.setText(deck.name)

        var nameCatalog: MutableList<String> = ArrayList()
        var nameAdapter = ArrayAdapter(requireContext(), R.layout.list_item, nameCatalog)
        (deckFaceCardTextView.editText as? AutoCompleteTextView)?.setAdapter(nameAdapter)
        deckFaceCardTextView.editText?.doOnTextChanged { text, start, before, count ->
            if (text != null && text!!.length > 2) nameCatalog = dbHelper.getCardNameCatalog (text.toString())
            else nameCatalog = ArrayList()
            nameAdapter = ArrayAdapter(requireContext(), R.layout.list_item, nameCatalog)
            (deckFaceCardTextView.editText as? AutoCompleteTextView)?.setAdapter(nameAdapter)
        }

        saveButton.setOnClickListener {
            if (deckNameTextView.editText?.text.toString().length < 1) Toast.makeText(
                requireContext(),
                getString(R.string.no_deck_name_set),
                Toast.LENGTH_SHORT
            ).show()
            else dbHelper.updateDeckName(deckId, deckNameTextView.editText?.text.toString())

            if (deckFaceCardTextView.editText?.text.toString() != "") {
                val faceCardId = dbHelper.getCardIdFromName(deckFaceCardTextView.editText?.text.toString())
                if (faceCardId == null || faceCardId == "") Toast.makeText(requireContext(), getString(R.string.no_valid_face_card_name_set), Toast.LENGTH_SHORT).show()
                else {
                    val faceCard = dbHelper.getCard(faceCardId)
                    val faceCardImageUri = if (!faceCard.image_uris?.art_crop.toString().isNullOrEmpty()) faceCard.image_uris?.art_crop.toString() else faceCard.card_faces?.get(0)?.image_uris?.art_crop.toString()
                    dbHelper.updateFaceCardInDeck(deckId, faceCardImageUri)
                }
            }
        }
    }
}