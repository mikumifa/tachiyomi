package tachiyomi.presentation.core.i18n

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalContext
import dev.icerock.moko.resources.PluralsResource
import dev.icerock.moko.resources.StringResource
import tachiyomi.core.i18n.pluralStringResource
import tachiyomi.core.i18n.stringResource

//localize 本地化， 使用下面的函数来进行汉化 Internationalization => i18n
@Composable
@ReadOnlyComposable
fun stringResource(resource: StringResource): String {
    return LocalContext.current.stringResource(resource)
}

@Composable
@ReadOnlyComposable
fun stringResource(resource: StringResource, vararg args: Any): String {
    return LocalContext.current.stringResource(resource, *args)
}

@Composable
@ReadOnlyComposable
//得到复数的资源， 因为知识读取资源， 使用@ReadOnlyComposable
fun pluralStringResource(resource: PluralsResource, count: Int): String {
    //提供了应用程序的全局信息和资源
    return LocalContext.current.pluralStringResource(resource, count)
}

@Composable
@ReadOnlyComposable
fun pluralStringResource(resource: PluralsResource, count: Int, vararg args: Any): String {
    return LocalContext.current.pluralStringResource(resource, count, *args)
}
