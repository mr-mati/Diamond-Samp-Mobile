package com.mati.launcher.newUi.fragments

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mati.game.R
import com.mati.game.core.Config
import com.mati.game.core.GTASA
import com.mati.game.databinding.FragmentMainBinding
import com.mati.launcher.activity.LoaderActivity
import com.mati.launcher.activity.UpdateActivity
import com.mati.launcher.data.model.Update
import com.mati.launcher.utils.Interface
import com.mati.weikton.reg.Preferences
import org.ini4j.Ini
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FilenameFilter
import java.io.IOException

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        InitLogic()
        LoadNick()
        CheckNewUpdate()

        if (CheckFile()) {
            CeloeErrorDialog()
        }


        binding.setting.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_settingFragment)
        }

        binding.PlayGame.setOnClickListener {
            onClickPlay()
        }


    }

    fun onClickPlay() {
        if (CheckFile()) {
            CeloeErrorDialog()
        } else {
            if (IsGameInstalled()) {
                if (IsUpdateInstalled()) {
                    try {
                        val ini = Ini(
                            File(
                                Environment.getExternalStorageDirectory()
                                    .toString() + "/PersianRp/version.ini"
                            )
                        )
                        if (ini["version", "code"] == Config.VERSION_CODE_DATA) {
                            startActivity(Intent(requireContext(), GTASA::class.java))
                            if (checkValidNick()) {
                                startActivity(Intent(requireActivity(), GTASA::class.java))
                            } else {
                                checkValidNick()
                            }
                        } else {
                            ToUpdate()
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                } else {
                    ToUpdate()
                }
            } else {
                ToLoad()
            }
        }
    }

    private fun IsGameInstalled(): Boolean {
        val CheckFile =
            Environment.getExternalStorageDirectory().toString() + "/PersianRp/texdb/gta3.img"
        val file = File(CheckFile)
        return file.exists()
    }

    fun CheckFile(): Boolean {
        val directoryPath = Environment.getExternalStorageDirectory().toString() + "/PersianRp/"
        val directoryCloe = Environment.getExternalStorageDirectory().toString() + "/PersianRp/cloe"
        val directoryData = Environment.getExternalStorageDirectory().toString() + "/PersianRp/data"
        val directorySamp = Environment.getExternalStorageDirectory().toString() + "/PersianRp/samp"
        val directoryAnim = Environment.getExternalStorageDirectory().toString() + "/PersianRp/anim"
        val directoryPersian =
            Environment.getExternalStorageDirectory().toString() + "/PersianRp/persian"
        val fileExtensions = arrayOf("cs", "asi", "csa", "csi", "so")

        return if (hasFiles(directoryAnim, fileExtensions) || hasFiles(
                directoryPath,
                fileExtensions
            ) || hasFiles(directoryCloe, fileExtensions) || hasFiles(
                directoryData,
                fileExtensions
            ) || hasFiles(directorySamp, fileExtensions) || hasFiles(
                directoryPersian,
                fileExtensions
            )
        ) {
            true
        } else {
            false
        }
    }

    fun hasFiles(directoryPath: String, fileExtensions: Array<String>): Boolean {
        val directory = File(directoryPath)
        val filter = FilenameFilter { _, name ->
            for (extension in fileExtensions) {
                if (name.endsWith(".$extension")) {
                    return@FilenameFilter true
                }
            }
            false
        }
        val files = directory.listFiles(filter)
        return files != null && files.size > 0
    }

    private fun tost(pon: String) {
        Toast.makeText(requireContext(), pon, Toast.LENGTH_LONG).show()
    }

    private fun IsUpdateInstalled(): Boolean {
        val CheckFile =
            Environment.getExternalStorageDirectory().toString() + "/PersianRp/version.ini"
        val file = File(CheckFile)
        return file.exists()
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
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun checkValidNick(): Boolean {
        val nick = binding.UserName
        if (nick.text.toString().isEmpty()) {
            tost("نام مستعار را وارد کنید")
            return false
        }
        if (!(nick.text.toString().contains("_"))) {
            tost("نام مستعار باید دارای نماد \"_\" باشد ")
            return false
        }
        if (nick.text.toString().length < 4) {
            tost(
                """
                
                نام مستعار باید حداقل 4 کاراکتر باشد
                """.trimIndent()
            )
            return false
        }
        return true
    }

    private fun InitLogic() {
        try {
            val w = Ini(
                File(
                    Environment.getExternalStorageDirectory()
                        .toString() + "/PersianRp/persian/settings.ini"
                )
            )
            binding.UserName.text = w["client", "name"]
            w.store()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun CheckNewUpdate() {
        val retrofit = Retrofit.Builder().baseUrl(Config.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()

        val sInterface = retrofit.create(
            Interface::class.java
        )
        val scall = sInterface.update
        scall.enqueue(object : Callback<Update> {
            override fun onResponse(call: Call<Update>, response: Response<Update>) {
                val data = response.body()!!
                    .values

                if (data[0] != null) {
                    if (data[0]!!.version_code != Config.VERSION_CODE) {
                        UpdateDialog(data[0]!!.mandatory)
                    }
                } else {
                    tost("خطا در اتصال با سروور")
                }
            }

            override fun onFailure(call: Call<Update>, t: Throwable) {
                ErrorDialog()
            }
        })
    }

    private fun ToLoad() {
        val intent = Intent(requireContext(), LoaderActivity::class.java)
        startActivity(intent)
    }

    private fun ToUpdate() {
        val intent = Intent(requireContext(), UpdateActivity::class.java)
        startActivity(intent)
    }

    private fun UpdateDialog(visible: Int) {
        val dialog = AlertDialog.Builder(requireContext()).create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setView(layoutInflater.inflate(R.layout.update_dialog, null))
        dialog.setCancelable(false)
        dialog.show()

        val btnCancel = dialog.findViewById<ConstraintLayout>(R.id.cancel_btn)
        val btnUpdate = dialog.findViewById<ConstraintLayout>(R.id.Update_Btn)

        if (visible == 0) {
            btnCancel!!.visibility = View.VISIBLE
        } else {
            btnCancel!!.visibility = View.GONE
        }

        btnCancel.setOnClickListener { dialog.dismiss() }

        btnUpdate!!.setOnClickListener {
            val telegram =
                Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/Mati_Source"))
            telegram.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            telegram.setPackage("org.telegram.messenger")
            startActivity(telegram)
        }
    }

    private fun ErrorDialog() {
        val dialog = AlertDialog.Builder(requireContext()).create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setView(layoutInflater.inflate(R.layout.error_dialog, null))
        dialog.setCancelable(false)
        dialog.show()

        val btnUpdate = dialog.findViewById<ConstraintLayout>(R.id.Update_Btn)

        btnUpdate!!.setOnClickListener {
            requireActivity().recreate()
        }
    }


    private fun CeloeErrorDialog() {
        val dialog = AlertDialog.Builder(requireContext()).create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setView(layoutInflater.inflate(R.layout.cheat_dialog, null))
        dialog.setCancelable(false)
        dialog.show()

        val btnUpdate = dialog.findViewById<ConstraintLayout>(R.id.Update_Btn)

        btnUpdate!!.setOnClickListener {
            requireActivity().recreate()
        }
    }


}