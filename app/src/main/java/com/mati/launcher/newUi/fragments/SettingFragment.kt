package com.mati.persianlauncher.fragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.mati.game.databinding.FragmentSettingBinding
import com.mati.game.databinding.ItemChangeNikBinding
import com.mati.weikton.reg.Preferences
import org.ini4j.Ini
import java.io.File
import java.io.IOException

class SettingFragment : Fragment() {
    private lateinit var binding: FragmentSettingBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSettingBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        LoadNick()

        binding.btnEdit.setOnClickListener {
            changeNameDialog()
        }


    }

    private fun changeNameDialog() {
        val dialog = AlertDialog.Builder(requireContext()).create()
        val dialogBinding = ItemChangeNikBinding.inflate(layoutInflater)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setView(dialogBinding.root)
        dialog.setCancelable(false)
        dialog.show()

        val btnCancel = dialogBinding.cancel
        val btnSave = dialogBinding.save

        btnCancel.setOnClickListener { dialog.dismiss() }

        btnSave.setOnClickListener {
            if (checkValidNick(dialogBinding.editText)) {
                try {
                    val f = File(
                        Environment.getExternalStorageDirectory()
                            .toString() + "/PersianRp/persian/settings.ini"
                    )
                    if (!f.exists()) {
                        f.createNewFile()
                        f.mkdirs()
                    }
                    val w = Ini(
                        File(
                            Environment.getExternalStorageDirectory()
                                .toString() + "/PersianRp/persian/settings.ini"
                        )
                    )
                    w.put("client", "name", dialogBinding.editText.text)
                    w.store()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            } else {
                checkValidNick(dialogBinding.editText)
            }
        }
    }

    private fun LoadNick() {
        try {
            val w = Ini(
                File(
                    Environment.getExternalStorageDirectory()
                        .toString() + "/PersianRp/persian/settings.ini"
                )
            )
            Preferences.setNick(w["client", "name"])
            w.store()
            binding.userName.text = w["client", "name"]
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun checkValidNick(editText: EditText): Boolean {
        if (editText.text.toString().isEmpty()) {
            tost("نام مستعار را وارد کنید")
            return false
        }
        if (!(editText.text.toString().contains("_"))) {
            tost("نام مستعار باید دارای نماد \"_\" باشد ")
            return false
        }
        if (editText.text.toString().length < 4) {
            tost(
                """
                
                نام مستعار باید حداقل 4 کاراکتر باشد
                """.trimIndent()
            )
            return false
        }
        return true
    }

    private fun tost(pon: String) {
        Toast.makeText(requireContext(), pon, Toast.LENGTH_LONG).show()
    }

}