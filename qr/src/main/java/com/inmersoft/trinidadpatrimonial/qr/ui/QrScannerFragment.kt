package com.inmersoft.trinidadpatrimonial.qr.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.inmersoft.trinidadpatrimonial.qr.databinding.QrScannerFragmentBinding

class QrScannerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return QrScannerFragmentBinding.inflate(inflater, container, false).root
    }
}