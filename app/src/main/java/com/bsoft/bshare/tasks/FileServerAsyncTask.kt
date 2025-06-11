package com.bsoft.bshare.tasks

import java.util.concurrent.Callable
import java.util.concurrent.FutureTask

class FileServerAsyncTask(callable: Callable<String?>?) : FutureTask<String?>(callable) {
}