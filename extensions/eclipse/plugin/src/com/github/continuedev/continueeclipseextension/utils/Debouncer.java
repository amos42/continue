package com.github.continuedev.continueeclipseextension.utils;

import java.util.concurrent.CancellationException;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineStart;
import kotlinx.coroutines.DelayKt;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.Job.DefaultImpls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Debouncer {
   private final long interval;
   @NotNull
   private final CoroutineScope coroutineScope;
   @Nullable
   private Job debounceJob;

   public Debouncer(long interval, @NotNull CoroutineScope coroutineScope) {
      Intrinsics.checkNotNullParameter(coroutineScope, "coroutineScope");
      super();
      this.interval = interval;
      this.coroutineScope = coroutineScope;
   }

   public final void debounce(@NotNull final Function1 action) {
      Intrinsics.checkNotNullParameter(action, "action");
      Job var2 = this.debounceJob;
      if (var2 != null) {
         DefaultImpls.cancel$default(var2, (CancellationException)null, 1, (Object)null);
      }

      this.debounceJob = BuildersKt.launch$default(this.coroutineScope, (CoroutineContext)null, (CoroutineStart)null, new Function2((Continuation)null) {
         int label;

         public final Object invokeSuspend(Object $result) {
            Object var2 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            switch (this.label) {
               case 0:
                  ResultKt.throwOnFailure($result);
                  long var10000 = Debouncer.this.interval;
                  Continuation var10001 = (Continuation)this;
                  this.label = 1;
                  if (DelayKt.delay(var10000, var10001) == var2) {
                     return var2;
                  }
                  break;
               case 1:
                  ResultKt.throwOnFailure($result);
                  break;
               case 2:
                  ResultKt.throwOnFailure($result);
                  return Unit.INSTANCE;
               default:
                  throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }

            Function1 var3 = action;
            this.label = 2;
            if (var3.invoke(this) == var2) {
               return var2;
            } else {
               return Unit.INSTANCE;
            }
         }

         public final Continuation create(Object value, Continuation $completion) {
            return (Continuation)(new <anonymous constructor>($completion));
         }

         public final Object invoke(CoroutineScope p1, Continuation p2) {
            return ((<undefinedtype>)this.create(p1, p2)).invokeSuspend(Unit.INSTANCE);
         }

         // $FF: synthetic method
         // $FF: bridge method
         public Object invoke(Object p1, Object p2) {
            return this.invoke((CoroutineScope)p1, (Continuation)p2);
         }
      }, 3, (Object)null);
   }
}
