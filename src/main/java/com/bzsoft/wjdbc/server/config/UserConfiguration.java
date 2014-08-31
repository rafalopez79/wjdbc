package com.bzsoft.wjdbc.server.config;

import java.util.ArrayList;
import java.util.List;

public class UserConfiguration {

	public static class Database {
		private String	ref;

		public Database() {
			// empty
		}

		public Database(final String ref) {
			this.ref = ref;
		}

		public String getRef() {
			return ref;
		}

		public void setRef(final String ref) {
			this.ref = ref;
		}

		@Override
		public String toString() {
			return ref;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (ref == null ? 0 : ref.hashCode());
			return result;
		}

		@Override
		public boolean equals(final Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			final Database other = (Database) obj;
			if (ref == null) {
				if (other.ref != null) {
					return false;
				}
			} else if (!ref.equals(other.ref)) {
				return false;
			}
			return true;
		}

	}

	private String						login;
	private String						password;
	private final List<Database>	databasesList;

	public UserConfiguration() {
		databasesList = new ArrayList<Database>();
	}

	public final String getLogin() {
		return login;
	}

	public final void setLogin(final String login) {
		this.login = login == null ? null : login.toLowerCase();
	}

	public final String getPassword() {
		return password;
	}

	public final void setPassword(final String password) {
		this.password = password;
	}

	public final List<Database> getDatabases() {
		return databasesList;
	}

	public final void addDatabase(final Database ref) {
		databasesList.add(ref);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (login == null ? 0 : login.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final UserConfiguration other = (UserConfiguration) obj;
		if (login == null) {
			if (other.login != null) {
				return false;
			}
		} else if (!login.equals(other.login)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "UserConfiguration [login=" + login + ", password=" + password + ", databasesList=" + databasesList + "]";
	}

}
