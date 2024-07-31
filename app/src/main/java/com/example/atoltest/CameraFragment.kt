package com.example.atoltest

import android.annotation.SuppressLint
import android.hardware.Camera
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.atoltest.databinding.FragmentCameraBinding
import java.io.IOException

/**
 * Main fragment for this app. Implements all camera operations including:
 * - Viewfinder
 * - Photo taking
 * - Image analysis
 */
class CameraFragment : Fragment() {

    private var _fragmentCameraBinding: FragmentCameraBinding? = null

    private val fragmentCameraBinding get() = _fragmentCameraBinding!!

    override fun onResume() {
        super.onResume()
        // Make sure that all permissions are still present, since the
        // user could have removed them while the app was in paused state.
//        if (!PermissionsFragment.hasPermissions(requireContext())) {
//            Navigation.findNavController(requireActivity(), R.id.fragment_container).navigate(
//                CameraFragmentDirections.actionCameraToPermissions()
//            )
//        }
        init()
    }

    override fun onDestroyView() {
        super.onDestroyView()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragmentCameraBinding = FragmentCameraBinding.inflate(inflater, container, false)
        return fragmentCameraBinding.root
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private var camera: Camera? = null
    private var cameraParameters: Camera.Parameters? = null
    private var surfaceView: SurfaceView? = null
    private var surfaceHolder: SurfaceHolder? = null
    private fun init() {
        this.surfaceView = _fragmentCameraBinding?.cameraSurfaceView
        surfaceHolder = surfaceView?.getHolder()
        surfaceHolder?.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                if (start()) {
                } else {
                }
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                stop()
            }
        })
        _fragmentCameraBinding!!.button1.setOnClickListener {
            camera!!.startPreview()

        }
        _fragmentCameraBinding!!.button.setOnClickListener {
            cameraParameters = camera!!.parameters
            camera!!.parameters = cameraParameters
            camera!!.autoFocus { success: Boolean, camera1: Camera ->
                if (success) {
                    camera1.takePicture({ Log.d("shutter","shutter")}, null,{a,b->
                        Log.d("success","success")
                    })
                } else {
                    Log.d("failed","focus")
                }
            }

        }
    }

    private fun start(): Boolean {
        try {
            camera = Camera.open()
            camera?.setPreviewDisplay(surfaceHolder)
        } catch (e: IOException) {
            return false
        } catch (e: RuntimeException) {
            return false
        }
        camera?.setDisplayOrientation(90)
        cameraParameters = camera?.getParameters()
        cameraParameters!!.focusMode = android.hardware.Camera.Parameters.FOCUS_MODE_AUTO
        camera?.setParameters(cameraParameters)
        camera?.startPreview()
        return true
    }

    fun stop() {
        if (camera != null) {
            camera?.stopPreview()
            camera?.release()
            camera = null
        }

    }

}