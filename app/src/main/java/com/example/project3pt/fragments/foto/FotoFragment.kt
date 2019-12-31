package com.example.project3pt.fragments.foto

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Environment.*
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.project3pt.R
import com.example.project3pt.databinding.FragmentFotoBinding
import com.example.project3pt.models.Foto
import java.util.*
import org.jetbrains.anko.support.v4.toast
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.*

class FotoFragment : Fragment() {

    private val vm: FotoViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentFotoBinding = DataBindingUtil.inflate(
        inflater, R.layout.fragment_foto, container, false
        )
        binding.geenfotos.visibility = View.INVISIBLE
        vm.getFotos()

        binding.refreshFotoLijst.setOnRefreshListener {
            vm.resetFotos()
            binding.geenfotos.visibility = View.INVISIBLE
            binding.loading.visibility = View.VISIBLE
            vm.getFotos()
            binding.refreshFotoLijst.isRefreshing = false
        }

        val adapter =
            FotoAdapter(FotoListener {
                openFotoDialog(it)
            })
        binding.fotoLijst.adapter = adapter

        vm.fotos.observe(viewLifecycleOwner, Observer {
                adapter.submitList(it)
            binding.loading.visibility = View.INVISIBLE
            if(it.isNullOrEmpty()){
                binding.geenfotos.visibility = View.VISIBLE
            }
        })

        binding.floatingActionButton.setOnClickListener {
            pickFromGallery()
        }

        return binding.root
    }

    private fun openFotoDialog(foto: Foto) {
        val builder = AlertDialog.Builder(requireContext())
        // Get the layout inflater
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.foto_dialog, null)

        val cancel: ImageView? = view.findViewById(R.id.close)
        val download: ImageView? = view.findViewById(R.id.download)
        val fotoDialog: ImageView? = view.findViewById(R.id.foto_dialog)
        fotoDialog?.setImageBitmap(foto.getImage())

        builder.setView(view)
        val dialog = builder.create()

        cancel?.setOnClickListener {
            dialog.cancel()
        }
        download?.setOnClickListener {
            if (askExternalWritePermission()) {
                saveImageToExternalStorage(foto.getImage())
            }
        }
        dialog.show()
    }

    private fun askExternalWritePermission(): Boolean {
            if (checkSelfPermission(activity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(activity!!, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
                return false
            } else {
                return true
            }
    }

    private fun saveImageToExternalStorage(bitmap: Bitmap): Uri {
        // Get the external storage directory path
        val path = getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS)
        Log.i("path", path.toString())
        // Create a file to save the image
        val file = File(path, "000" + "${UUID.randomUUID()}.jpg")
        try {
            // Get the file output stream
            val stream: OutputStream = FileOutputStream(file)
            // Compress the bitmap
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            // Flush the output stream
            stream.flush()

            // Close the output stream
            stream.close()
            toast("Image saved successful.")
            var filesToScan = Array(1) { file.absolutePath }
            MediaScannerConnection.scanFile(activity!!, filesToScan, null, { s, uri -> })
            makeNotification(file.absolutePath)
        } catch (e: IOException) { // Catch the exception
            e.printStackTrace()
            toast("Error to save image.")
        }
        // Return the saved image path to uri
        return Uri.parse(file.absolutePath)
    }

    private fun makeNotification(path: String) {
        Log.i("path file", Uri.parse(path).toString())
        val channel = NotificationChannel("notification", "Channel for fotos", NotificationManager.IMPORTANCE_DEFAULT)

        var intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.setDataAndType(Uri.parse(path), "image/*")
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

        val notification = NotificationCompat.Builder(requireContext(), "notification")
            .setSmallIcon(R.drawable.ic_photo)
            .setContentTitle("foto is gedownload")
            .setContentText("bekijk je nieuwe foto")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .addAction(R.drawable.ic_photo, "", PendingIntent.getActivity(requireContext(), 0, intent, 0))
            .setContentIntent(PendingIntent.getActivity(requireContext(), 0, intent, 0)).build()

        val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
        Log.i("noti", notificationManager.toString())

        notificationManager.notify(100, notification)
    }

    private val PICK_IMAGE = 1

    private fun pickFromGallery() {
        var getIntent = Intent(Intent.ACTION_GET_CONTENT)
        getIntent.type = "image/*";

        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.type = "image/*"

    val chooserIntent = Intent.createChooser(getIntent, "Select Image")
    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,  Array(1){pickIntent})

    startActivityForResult(pickIntent, PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICK_IMAGE){
            if(data == null){
                return
            }
            val inputStream = requireContext().contentResolver.openInputStream(data.getData()!!)
            val file = BitmapFactory.decodeStream(inputStream)
            saveFotoDialog(file)
        }
    }

    private fun saveFotoDialog(bitmap: Bitmap) {
        val builder = AlertDialog.Builder(requireContext())
            .setTitle("Wens u deze foto toe te voegen?")
            .setPositiveButton("ja",{ dialogInterface, i -> postFoto(bitmap) })
            .setNegativeButton("neen", {dialogInterface, i ->  dialogInterface.cancel()})
        // Get the layout inflater
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.save_foto_dialog, null)
        val fotoDialog: ImageView? = view.findViewById(R.id.foto_dialog)
        fotoDialog?.setImageBitmap(bitmap)

        builder.setView(view)
        val dialog = builder.create()

        dialog.show()
    }

    fun postFoto(bitmap: Bitmap){
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val imageBytes: ByteArray = baos.toByteArray()
        val base64 = Base64.encodeToString(imageBytes, Base64.NO_WRAP)
        vm.addFoto(Foto(base64))
    }


}
