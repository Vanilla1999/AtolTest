package com.example.atoltest

import Dialog.adapters.BTDevice
import Dialog.adapters.BluetoothBoundAdapter
import Dialog.adapters.BluetoothNotBoundAdapter
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.*
import android.content.pm.ResolveInfo
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import java.io.IOException
import java.util.*
import kotlin.coroutines.CoroutineContext


class DialogfragmentBluetoothNew : DialogFragment(), CoroutineScope {
    private lateinit var mContext: Context
    private var button: View? = null
    private var recyclerViewBound: RecyclerView? = null
    private var recyclerViewNotBound: RecyclerView? = null

    var arrayListBluetoothDevices = ArrayList<BluetoothDevice>()
    var arrayListPairedBluetoothDevices = ArrayList<BluetoothDevice>()
    private var myUUID: UUID? = null
    var bdDevice: BluetoothDevice? = null
    var bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    var filter3 = IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
    var toast: Toast? = null
    private lateinit var adapterNotBound: BluetoothNotBoundAdapter
    private lateinit var adapterBound: BluetoothBoundAdapter

    override fun onAttach(context: Activity) {
        super.onAttach(context)
        this.mContext = context
    }

    @SuppressLint("MissingPermission")
    override fun onDestroy() {
        super.onDestroy()
        bluetoothAdapter.cancelDiscovery()
        mContext.unregisterReceiver(receiver)
        mContext.unregisterReceiver(receiver1)
        mContext.unregisterReceiver(receiverForScanEnd)
        mContext.unregisterReceiver(receiverWindow)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.blutoothwindow, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Get field from view
        recyclerViewBound = view.findViewById(R.id.recyclerViewBound) as RecyclerView
        recyclerViewNotBound = view.findViewById(R.id.recyclerViewNotBound) as RecyclerView
        button = view.findViewById(R.id.button) as Button


        val ifl = IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
        ifl.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST)
        mContext.registerReceiver(receiverWindow, ifl)

        button!!.setOnClickListener { dismiss() }
        initAdapters()
        myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        mContext.registerReceiver(receiver, filter)

        initializePeripheralsListView()
        pairedDevices

        val filter1 = IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
        // когда сопряжение происходит
        mContext.registerReceiver(receiver1, filter1)
        mContext.registerReceiver(receiverForScanEnd, filter3)
        //        myThread = new MyThread();
//        myThread.start();
    }

    private val receiverWindow: BroadcastReceiver = object : BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            val device =
                intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
            if (BluetoothDevice.ACTION_PAIRING_REQUEST == action) {
                if (device?.name?.contains("WR10") == false)
                    abortBroadcast()
            } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED == action) {
                val bondState =
                    intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR)
                if (bondState == BluetoothDevice.BOND_BONDING && device?.name?.contains("WR10") == false) {
                    device.setPin("0000".toByteArray())
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun connectDevice(btDevice: BTDevice) {
        bdDevice =
            arrayListBluetoothDevices.find { it.address == btDevice.macAdress && it.name == btDevice.deviceName }
        bdDevice?.createBond();

        val list =
            adapterNotBound.btList.filterNot { it.deviceName == btDevice.deviceName && it.macAdress == btDevice.macAdress }
        arrayListBluetoothDevices.remove(bdDevice)
        adapterNotBound.update(list)
    }

    @SuppressLint("MissingPermission")
    private fun removeBound(btDevice: BTDevice) {

        bdDevice =
            arrayListPairedBluetoothDevices.find { it.address == btDevice.macAdress && it.name == btDevice.deviceName }
        val removeBonding = removeBond(bdDevice)
        if (removeBonding) {
            arrayListPairedBluetoothDevices.remove(bdDevice)
            val list =
                adapterBound.btList.filterNot { it.deviceName == btDevice.deviceName && it.macAdress == btDevice.macAdress }
            adapterBound.update(list)

        }
    }

    private fun initAdapters() {
        adapterNotBound = BluetoothNotBoundAdapter {
            connectDevice(it)
        }
        adapterBound = BluetoothBoundAdapter({
            removeBound(it)
        }, {
        })

        adapterBound.setDiff(diffUtil)
        adapterNotBound.setDiff(diffUtil)
        recyclerViewBound!!.adapter = adapterBound
        recyclerViewNotBound!!.adapter = adapterNotBound
        recyclerViewBound!!.addItemDecoration(
            DividerItemDecoration(
                mContext,
                LinearLayoutManager.VERTICAL
            )
        )
        recyclerViewNotBound!!.addItemDecoration(
            DividerItemDecoration(
                mContext,
                LinearLayoutManager.VERTICAL
            )
        )
    }

    private val receiver1: BroadcastReceiver = object : BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context, intent: Intent) {
            val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
            device?.let { deviceNotNull ->
                if (deviceNotNull.bondState == BluetoothDevice.BOND_BONDED) {
                    val peripheral = BTDevice(deviceNotNull.address, deviceNotNull.name)
                    if (adapterBound.btList.firstOrNull
                        { it.deviceName == peripheral.deviceName && it.macAdress == peripheral.macAdress } == null
                    ) {
                        arrayListPairedBluetoothDevices.add(deviceNotNull)
                        adapterBound.updateOneItemDiff(
                            BTDevice(
                                deviceNotNull.address,
                                deviceNotNull.name
                            )
                        )
                    }
                }
            }
        }
    }
    private val receiverForScanEnd: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            // bluetoothAdapter.startDiscovery();
//            Message message = mHandler.obtainMessage();
//            message.what = msg_start_discover;
//            mHandler.sendMessage(message);
        }
    }

    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (BluetoothDevice.ACTION_FOUND == action) {
                val device =
                    intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                device?.let { deviceNotNull ->
                    val peripheral = BTDevice(deviceNotNull.address, deviceNotNull.name)
                    if (adapterNotBound.btList.firstOrNull { it.deviceName == peripheral.deviceName && it.macAdress == peripheral.macAdress } == null &&
                        adapterBound.btList.firstOrNull { it.deviceName == peripheral.deviceName && it.macAdress == peripheral.macAdress } == null) {
                        arrayListBluetoothDevices.add(deviceNotNull)
                        adapterNotBound.updateOneItem(
                            BTDevice(
                                deviceNotNull.address,
                                deviceNotNull.name
                            )
                        )
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun initializePeripheralsListView() {
        // список доступных устройств блютуссссс
        bluetoothAdapter.startDiscovery() // поиск крч устройств.
    }

    @Throws(Exception::class)
    fun createBond(btDevice: BluetoothDevice?): Boolean {
        val class1 = Class.forName("android.bluetooth.BluetoothDevice")
        val createBondMethod = class1.getMethod("createBond")
        return createBondMethod.invoke(btDevice) as Boolean
    }

    @Throws(Exception::class)
    fun removeBond(btDevice: BluetoothDevice?): Boolean {
        val btClass = Class.forName("android.bluetooth.BluetoothDevice")
        val removeBondMethod = btClass.getMethod("removeBond")
        return removeBondMethod.invoke(btDevice) as Boolean
    }

    // типа список для связанных
    private val pairedDevices: Unit
        @SuppressLint("MissingPermission")
        get() {
            val pairedDevice = bluetoothAdapter.bondedDevices
            val listPair = mutableListOf<BTDevice>()
            if (pairedDevice.size > 0) {
                pairedDevice.forEach { bluetoothDevice ->
                    listPair.add(
                        BTDevice(
                            macAdress = bluetoothDevice.address,
                            deviceName = bluetoothDevice.name
                        )
                    )
                    arrayListPairedBluetoothDevices.add(bluetoothDevice)
                }
                adapterBound.update(listPair)

            }
        }


    private fun connectDeviceOld(mac: String) {
        try {
            val eintent: Intent = Intent(
                getExplicitIntent(
                    mContext,
                    Intent(ACTION_WEDGE_SERVICE)
                )
            )
            eintent.putExtra(DEVICE_ADDRESS, mac)
            eintent.putExtra(CONNECT_FROM_SETTINGS, 1)
            mContext.startService(eintent)
        } catch (e: java.lang.Exception) {
            Log.e("ZZ", "Start smartPOSDeviceBackendService failed:" + e.message)
        }
    }

    fun getExplicitIntent(context: Context, implicitIntent: Intent?): Intent? {
        val resolveInfo = context.packageManager.queryIntentServices(
            implicitIntent!!, 0
        )
        if (resolveInfo == null || resolveInfo.size != 1) {
            return null
        }
        val serviceInfo = resolveInfo[0]
        val component =
            ComponentName(serviceInfo.serviceInfo.packageName, serviceInfo.serviceInfo.name)
        val explicitIntent = Intent(implicitIntent)
        explicitIntent.component = component
        return explicitIntent
    }

    private fun connectDevice(context: Context, address: String) {
        Log.i("Log", "ДОШЛО ИЛИ НЕТ")
        if (!TextUtils.isEmpty(address) && BluetoothAdapter.checkBluetoothAddress(address)) {
            try {
                val intentService = Intent("android.intent.action.BTSCANNER_SERVICE")
                val eintent = Intent(getExplicitIntent(context, intentService))
                if (eintent != null) {
                    eintent.putExtra("device_address", address)
                    context.startService(eintent)
                } else {
                    intentService.putExtra("device_address", address)
                    context.startService(eintent)
                }
            } catch (var5: Exception) {
                Log.e("BluetoothManager", "Start BluetoothManager failed:" + var5.message)
            }
        } else {
            Log.e(
                "BluetoothManager",
                " IllegalArgumentException = $address is not a valid Bluetooth address,MAC address must be upper case"
            )
        }
    }

    fun removeBondDevice(context: Context?, address: String): Boolean {
        return if (!TextUtils.isEmpty(address) && BluetoothAdapter.checkBluetoothAddress(
                address
            )
        ) {
            val intent = Intent("ACTION_REMOVE_DECODE_RESULT")
            intent.putExtra("REMOVE_AD", address)
            mContext.sendBroadcast(intent)
            true
        } else {
            Log.e(
                "BluetoothManager",
                " IllegalArgumentException = $address is not a valid Bluetooth address,MAC address must be upper case"
            )
            false
        }
    }

    private fun cancel(adress: String) {
        if (toast != null) toast!!.cancel()
        Toast.makeText(context, "Close - BluetoothSocket", Toast.LENGTH_LONG)
        toast!!.show()
        removeBondDevice(context, adress)
        try {
            //bluetoothSocket.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    private val diffUtil = object : GenericItemDiff<BTDevice> {
        override fun isSame(
            oldItems: List<BTDevice>,
            newItems: List<BTDevice>,
            oldItemPosition: Int,
            newItemPosition: Int,
        ): Boolean {
            val oldData = oldItems[oldItemPosition]
            val newData = newItems[newItemPosition]

            return oldData == newData
        }

        override fun isSameContent(
            oldItems: List<BTDevice>,
            newItems: List<BTDevice>,
            oldItemPosition: Int,
            newItemPosition: Int,
        ): Boolean {
            return oldItems[oldItemPosition].macAdress == newItems[newItemPosition].macAdress &&
                    oldItems[oldItemPosition].deviceName == newItems[newItemPosition].deviceName
        }
    }

    companion object {
        protected var mSettings: SharedPreferences? = null
        fun getExplicitIntent(context: Context, implicitIntent: Intent): Intent? {
            val pm = context.packageManager
            val resolveInfo = pm.queryIntentServices(implicitIntent, 0)
            return if (resolveInfo != null && resolveInfo.size == 1) {
                val serviceInfo = resolveInfo[0] as ResolveInfo
                val packageName = serviceInfo.serviceInfo.packageName
                val className = serviceInfo.serviceInfo.name
                val component = ComponentName(packageName, className)
                val explicitIntent = Intent(implicitIntent)
                explicitIntent.component = component
                explicitIntent
            } else {
                null
            }
        }
    }

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.IO


    val ACTION_WEDGE_SERVICE = "android.intent.action.BTSCANNER_SERVICE"
    val DEVICE_ADDRESS = "device_address"
    val CONNECT_FROM_SETTINGS = "connect_from_settings"
    val ACTION_CONNECTION_STATE = "android.bluetooth.device.urovo.connected"
    val ACTION_FORCE_CONNECTED_DEVICE = "ACTION_FORCE_CONNECTED_DEVICE"

}