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
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mati.game.R
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
        loadVoice()
        loadData()
        loadFps()

        binding.btnEdit.setOnClickListener {
            changeNameDialog()
        }

        binding.btnClose.setOnClickListener {
            requireActivity().finish()
        }

        binding.btnDeleteData.setOnClickListener {

        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.action_settingFragment_to_mainFragment)
                }
            })

    }

    private fun changeNameDialog() {
        val dialog = AlertDialog.Builder(requireContext()).create()
        val dialogBinding = ItemChangeNikBinding.inflate(layoutInflater)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setView(dialogBinding.root)
        dialog.setCancelable(false)
        dialog.show()

        lateinit var w: Ini

        try {
            w = Ini(
                File(
                    Environment.getExternalStorageDirectory()
                        .toString() + "/PersianRp/persian/settings.ini"
                )
            )
            Preferences.setNick(w["client", "name"])
            w.store()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        dialogBinding.editText.setText(w["client", "name"])

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
                    w.put("client", "name", dialogBinding.editText.text)
                    w.store()
                    Toast.makeText(
                        requireContext(),
                        " نام کاربری شما تغییر کرد به : ${dialogBinding.editText.text} ",
                        Toast.LENGTH_SHORT
                    ).show()
                    dialog.dismiss()
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

    private fun loadVoice() {
        try {
            val w = Ini(
                File(
                    Environment.getExternalStorageDirectory()
                        .toString() + "/PersianRp/persian/settings.ini"
                )
            )
            Preferences.setNick(w["gui", "fps"])
            w.store()

            when (w["client", "name"]) {
                "1" -> {
                    binding.txtSelectVoiceChat.text = "روشن"
                    binding.txtSelectVoiceChat.setTextColor(Color.parseColor("#2ECC71"))
                }

                "0" -> {
                    binding.txtSelectVoiceChat.text = "خاموش"
                    binding.txtSelectVoiceChat.setTextColor(Color.parseColor("#f0655e"))
                }
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun loadData() {
        if (IsGameInstalled()) {
            if (IsUpdateInstalled()) {
                binding.txtSelectGtaData.text = "دیتا یافت شد"
                binding.txtSelectGtaData.setTextColor(Color.parseColor("#2ECC71"))
            } else {
                binding.txtSelectGtaData.text = "نیاز به بروزرسانی دیتا"
                binding.txtSelectGtaData.setTextColor(Color.parseColor("#FF8600"))
            }
        } else {
            binding.txtSelectGtaData.text = "دیتا پیدا نشد"
            binding.txtSelectGtaData.setTextColor(Color.parseColor("#f0655e"))
        }
    }

    private fun loadFps() {
        try {
            val w = Ini(
                File(
                    Environment.getExternalStorageDirectory()
                        .toString() + "/PersianRp/persian/settings.ini"
                )
            )
            Preferences.setNick(w["gui", "fps"])
            w.store()

            when (w["client", "name"]) {
                "30" -> {
                    binding.radioNoLimit.isChecked = false
                    binding.radio30f.isChecked = true
                    binding.radio60f.isChecked = false
                }

                "60" -> {
                    binding.radioNoLimit.isChecked = false
                    binding.radio30f.isChecked = false
                    binding.radio60f.isChecked = true
                }

                else -> {
                    binding.radioNoLimit.isChecked = true
                    binding.radio30f.isChecked = false
                    binding.radio60f.isChecked = false
                }
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun IsGameInstalled(): Boolean {
        val CheckFile =
            Environment.getExternalStorageDirectory().toString() + "/PersianRp/texdb/gta3.img"
        val file = File(CheckFile)
        return file.exists()
    }

    private fun IsUpdateInstalled(): Boolean {
        val CheckFile =
            Environment.getExternalStorageDirectory().toString() + "/PersianRp/version.ini"
        val file = File(CheckFile)
        return file.exists()
    }

    private fun tost(pon: String) {
        Toast.makeText(requireContext(), pon, Toast.LENGTH_LONG).show()
    }

}