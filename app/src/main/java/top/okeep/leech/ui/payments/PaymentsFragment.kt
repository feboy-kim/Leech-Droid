package top.okeep.leech.ui.payments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import top.okeep.leech.databinding.PaymentsFragmentBinding
import top.okeep.leech.models.AppDatabase

class PaymentsFragment : Fragment() {
    private val _vm: PaymentsViewModel by viewModels()
    private val _db: AppDatabase by lazy {
        AppDatabase.getWorkDatabase(requireActivity().applicationContext)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = PaymentsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
}