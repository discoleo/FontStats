// Copyright (c) 2016 Leonard Mada and Syonic SRL
// leo.mada@syonic.eu
//
// This file is part of FontStats.
// 
// FontStats is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
// 
// FontStats is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with FontStats.  If not, see <http://www.gnu.org/licenses/>.
// 
// 

package tools;

public class ED<V1, V2> {
	public V1 E1;
	public V2 E2;
	
	// +++++++ CONSTRUCTOR ++++++++
	
	public ED(final V1 v1, final V2 v2) {
		E1 = v1;
		E2 = v2;
	}
	
	// ++++++++ MEMBER FUNCTIONS +++++++
	
	public boolean equals(final ED<V1, V2> other) {
		return (E1 == other.E1) && (E2 == other.E2);
	}
}