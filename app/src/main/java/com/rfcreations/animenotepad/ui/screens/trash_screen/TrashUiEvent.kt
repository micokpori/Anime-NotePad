package com.rfcreations.animenotepad.ui.screens.trash_screen

import com.rfcreations.animenotepad.database.trash.TrashEntity

sealed class TrashUiEvent{
    data class RestoreNote(val noteToRestore: TrashEntity): TrashUiEvent()
    data class PermanentDeleteTrashNote(val noteToDelete: TrashEntity): TrashUiEvent()
}
