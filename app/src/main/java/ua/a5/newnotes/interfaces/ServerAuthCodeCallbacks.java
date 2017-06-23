package ua.a5.newnotes.interfaces;

import com.google.android.gms.common.api.Scope;

import java.util.Set;

import static com.google.android.gms.common.internal.zzac.zzb;


public interface ServerAuthCodeCallbacks {
    CheckResult onCheckServerAuthorization(String var1, Set<Scope> var2);

    boolean onUploadServerAuthCode(String var1, String var2);

    public static class CheckResult {
        private boolean zzPO;
        private Set<Scope> zzPP;

        public static CheckResult newAuthNotRequiredResult() {
            return new CheckResult(false, (Set)null);
        }

        public static CheckResult newAuthRequiredResult(Set<Scope> requiredScopes) {
            zzb(requiredScopes != null && !requiredScopes.isEmpty(), "A non-empty scope set is required if further auth is needed.");
            return new CheckResult(true, requiredScopes);
        }

        private CheckResult(boolean requiresNewAuthCode, Set<Scope> requiredScopes) {
            this.zzPO = requiresNewAuthCode;
            this.zzPP = requiredScopes;
        }

        public boolean zzkN() {
            return this.zzPO;
        }

        public Set<Scope> zzkO() {
            return this.zzPP;
        }
    }
}