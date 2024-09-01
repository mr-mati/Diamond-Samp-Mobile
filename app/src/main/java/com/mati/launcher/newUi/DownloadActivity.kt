package com.mati.launcher.newUi

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hzy.libp7zip.P7ZipApi
import com.liulishuo.filedownloader.BaseDownloadTask
import com.liulishuo.filedownloader.FileDownloadSampleListener
import com.liulishuo.filedownloader.FileDownloader
import com.mati.game.core.Config.DOWNLOAD_URL
import com.mati.game.databinding.FragmentLoaderBinding
import com.mati.launcher.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class DownloadActivity : AppCompatActivity() {

    private lateinit var binding: FragmentLoaderBinding
    private lateinit var folder: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentLoaderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FileDownloader.setup(applicationContext)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        startDownload()
    }

    private fun startDownload() {
        folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        createDownloadTask(DOWNLOAD_URL, folder.path).start()
    }

    private fun createDownloadTask(url: String, path: String): BaseDownloadTask {
        return FileDownloader.getImpl().create(url)
            .setPath(path, true)
            .setCallbackProgressTimes(100)
            .setMinIntervalUpdateSpeed(100)
            .setListener(object : FileDownloadSampleListener() {

                override fun pending(task: BaseDownloadTask, soFarBytes: Int, totalBytes: Int) {
                    super.pending(task, soFarBytes, totalBytes)
                }

                override fun progress(task: BaseDownloadTask, soFarBytes: Int, totalBytes: Int) {
                    super.progress(task, soFarBytes, totalBytes)
                    val progressPercent = soFarBytes * 100L / totalBytes

                    binding.loadingText.text = "در حال بارگیری فایل های بازی..."
                    binding.loadingPercent.text = String.format(
                        "%s از %s",
                        Utils.bytesIntoHumanReadable(soFarBytes.toLong()),
                        Utils.bytesIntoHumanReadable(totalBytes.toLong())
                    )
                    binding.progressBar.progress = progressPercent.toInt().toFloat()
                }

                override fun error(task: BaseDownloadTask, e: Throwable) {
                    super.error(task, e)
                    Toast.makeText(this@DownloadActivity, e.message, Toast.LENGTH_SHORT).show()
                    Toast.makeText(
                        this@DownloadActivity,
                        "خطایی روی داد، لطفاً دوباره نصب را امتحان کنید",
                        Toast.LENGTH_SHORT
                    ).show()
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(5000)
                        recreate()
                    }
                }

                override fun connected(
                    task: BaseDownloadTask,
                    etag: String?,
                    isContinue: Boolean,
                    soFarBytes: Int,
                    totalBytes: Int
                ) {
                    super.connected(task, etag ?: "unknown", isContinue, soFarBytes, totalBytes)
                }

                override fun paused(task: BaseDownloadTask, soFarBytes: Int, totalBytes: Int) {
                    super.paused(task, soFarBytes, totalBytes)
                }

                override fun completed(task: BaseDownloadTask) {
                    super.completed(task)
                    binding.loadingText.text = "جعبه گشایی..."
                    binding.loadingPercent.text = "2/3"
                    unZipCache()
                }

                override fun warn(task: BaseDownloadTask) {
                    super.warn(task)
                }
            })
    }

    private fun unZipCache() {
        val inputFilePath =
            "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)}/DiamondMobile.7z"
        val outputPath = Environment.getExternalStorageDirectory().toString()

        CoroutineScope(Dispatchers.IO).launch {
            P7ZipApi.executeCommand("7z x '$inputFilePath' '-o$outputPath' -aoa")
            Utils.delete(File("${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)}/DiamondMobile.7z"))
            Utils.delete(File("${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)}/DiamondMobile.7z.temp"))

            withContext(Dispatchers.Main) {
                afterDownload()
            }
        }
    }

    private fun afterDownload() {
        binding.loadingText.text = "کمی صبر و تقوا پیشه کنید"
        binding.loadingPercent.text = "3/3"
        Toast.makeText(this, "بازی با موفقیت نصب شد!", Toast.LENGTH_SHORT).show()
        CoroutineScope(Dispatchers.Main).launch {
            delay(5000)
            val intent = Intent(this@DownloadActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
