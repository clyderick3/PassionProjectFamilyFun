import { IFamilyTree } from 'app/shared/model/family-tree.model';

export interface IJournaling {
  id?: number;
  title?: string | null;
  date?: string | null;
  journalEntry?: number | null;
  familyTrees?: IFamilyTree[] | null;
}

export const defaultValue: Readonly<IJournaling> = {};
