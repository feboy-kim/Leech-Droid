package top.okeep.leech.ui.subjects

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import top.okeep.leech.SubjectsActivity
import top.okeep.leech.databinding.SubjectEditFragmentBinding

class SubjectEditFragment : Fragment() {

    private val _vm: SubjectEditViewModel by viewModels()
    private val _dm: SubjectsActivity.DataViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val binding = SubjectEditFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = _vm
        binding.broker = Broker()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (_vm.original.value == null) {                    // Keep from loading when rotating
            _vm.loadSubject(_dm.db.subjectAccessor())
        }
        _vm.isValidlySaved.observe(viewLifecycleOwner) {
            if (it) {
                _dm.dataChanged = true
                findNavController().popBackStack()
            }
        }
    }

    inner class Broker {
        fun saveSubject(v: View) {
            _vm.saveSubject(_dm.db.subjectAccessor())
        }
    }
}